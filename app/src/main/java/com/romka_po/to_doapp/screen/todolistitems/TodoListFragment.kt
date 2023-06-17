package com.romka_po.to_doapp.screen.todolistitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.snackbar.Snackbar
import com.romka_po.to_doapp.R
import com.romka_po.to_doapp.databinding.FragmentTodoListBinding
import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.model.TodoListAdapter
import dagger.hilt.android.AndroidEntryPoint

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

        binding.navigateToAddFAB.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_addEditItem)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        rvAdapter = TodoListAdapter(
            shortClickListener = { todoItem ->
                navigateToAddEditFragment(todoItem)
            },
            longClickListener = { todoItem, i -> showMenu(todoItem, i) },
            checkboxClickListener = { todoItem ->
                viewModel.editTodoItem(todoItem)
                val temp = if (todoItem.isComplete) {
                    1
                } else {
                    -1
                }
                viewModel.countOfComplete.value = viewModel.countOfComplete.value!! + temp
            })
        with(recyclerView) {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
    }

    private fun provideObservers() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            rvAdapter.diffList.submitList(it)
        }
        viewModel.countOfComplete.observe(viewLifecycleOwner) {
            binding.countCompleteTextView.text = getString(R.string.complete,it)
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
                val position = viewHolder.adapterPosition
                deleteItem(deletedCourse, position)

            }
        }).attachToRecyclerView(recyclerView)
    }

    private fun showMenu(todoItem: TodoItem, position: Int) {
        val popup = PopupMenu(context, binding.todoRecyclerView[position])
        popup.menuInflater.inflate(R.menu.dropdown_item_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.itemEdit -> {
                    navigateToAddEditFragment(todoItem)
                }

                R.id.itemDelete -> {
                    deleteItem(todoItem, position)
                }
            }
            return@setOnMenuItemClickListener false
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    private fun navigateToAddEditFragment(todoItem: TodoItem) {
        findNavController().navigate(
            TodoListFragmentDirections.actionTodoListFragmentToAddEditItem(todoItem)
        )
    }

    private fun deleteItem(deletedCourse: TodoItem, position: Int) {
        viewModel.removeItemWithID(deletedCourse.id)
        rvAdapter.notifyDataSetChanged()
        Snackbar.make(
            binding.todoListConstraintLayout,
            "Deleted " + deletedCourse.text,
            Snackbar.LENGTH_LONG
        )
            .setAction(
                "Undo"
            ) {
                viewModel.addTodoItemAt(deletedCourse, position)
                rvAdapter.notifyDataSetChanged()
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}