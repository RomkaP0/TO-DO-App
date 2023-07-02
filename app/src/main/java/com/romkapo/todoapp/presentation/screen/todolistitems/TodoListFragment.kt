package com.romkapo.todoapp.presentation.screen.todolistitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.snackbar.Snackbar
import com.romkapo.todoapp.R
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.databinding.FragmentTodoListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private val viewModel: TodoItemListViewModel by viewModels()
    lateinit var rvAdapter: TodoListAdapter

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(binding.todoRecyclerView)
        swipeListener(binding.todoRecyclerView)

        provideObservers()
        viewModel.getAllList()

        binding.navigateToAddFAB.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_addEditItem)
        }

        with(binding.showUncheckedCheckbox) {
            isChecked = viewModel.showUnchecked
            setOnClickListener {
                viewModel.changeShow()
            }
        }
        binding.swipeLayoutLit.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.logout.setOnClickListener {
            viewModel.logOut()
            findNavController().navigate(R.id.action_todoListFragment_to_authFragment)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        rvAdapter = TodoListAdapter(
            shortClickListener = { todoItem ->
                navigateToAddEditFragment(todoItem.id)
            },
            longClickListener = { todoItem, i -> showMenu(todoItem, i) },
            checkboxClickListener = { todoItem ->
                viewModel.editStateTodoItem(todoItem)
            })

        with(recyclerView) {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
    }

    private fun provideObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listTodoItem.collectLatest {
                if (binding.showUncheckedCheckbox.isChecked) {
                    rvAdapter.diffList.submitList(it)
                } else {
                    rvAdapter.diffList.submitList(it.filter { item -> !item.isComplete })
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.countOfComplete.collectLatest {
                binding.countCompleteTextView.text = getString(R.string.complete, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest { result ->
                val text: String = if (result)
                    "Update successful"
                else
                    "Update failure"
                Snackbar.make(binding.swipeLayoutLit, text, Snackbar.LENGTH_SHORT).show()
                viewModel.getAllList()
                binding.swipeLayoutLit.isRefreshing = false
            }
        }

    }

    private fun swipeListener(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourse: TodoItem =
                    rvAdapter.diffList.currentList[viewHolder.adapterPosition]
                deleteItem(deletedCourse)
            }
        }).attachToRecyclerView(recyclerView)
    }

    private fun showMenu(todoItem: TodoItem, position: Int) {
        val popup = PopupMenu(context, binding.todoRecyclerView[position])
        popup.menuInflater.inflate(R.menu.dropdown_item_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.itemEdit -> {
                    navigateToAddEditFragment(todoItem.id)
                }

                R.id.itemDelete -> {
                    deleteItem(todoItem)
                }
            }
            return@setOnMenuItemClickListener false
        }

        popup.show()
    }

    private fun navigateToAddEditFragment(todoItemID: String) {
        findNavController().navigate(
            TodoListFragmentDirections.actionTodoListFragmentToAddEditItem(todoItemID)
        )
    }

    private fun deleteItem(deletedCourse: TodoItem) {
        viewModel.removeTodoItem(deletedCourse)

        Snackbar.make(
            binding.todoListConstraintLayout,
            getString(R.string.deleted) + deletedCourse.text,
            Snackbar.LENGTH_LONG
        )
            .setAction(
                getString(R.string.undo)
            ) {
                viewModel.addTodoItem(deletedCourse)
            }.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}