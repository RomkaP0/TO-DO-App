package com.romkapo.todoapp.presentation.screen.todolistitems

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.snackbar.Snackbar
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.databinding.FragmentTodoListBinding
import com.romkapo.todoapp.di.components.list.TodoListItemFragmentComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListFragment : Fragment() {
    private lateinit var rvAdapter: TodoListAdapter

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoFragmentComponent: TodoListItemFragmentComponent
    private val viewModel: TodoItemListViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        todoFragmentComponent = (requireContext().applicationContext as Application)
            .appComponent.todoItemListFragmentComponentFactory().create()
        todoFragmentComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeLayout.isRefreshing = false

        setupRecyclerView(binding.todoRecyclerView)

        provideObservers()
        viewModel.getAllList()
        setupListeners()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        rvAdapter = TodoListAdapter(
            editClickListener = { id -> navigateToAddEditFragment(id) },
            deleteClickListener = { todoItem -> deleteItem(todoItem) },
            checkboxClickListener = { todoItem -> viewModel.editStateTodoItem(todoItem) },
        )

        with(recyclerView) {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
        SwipeRightHelper(
            recyclerView,
            rvAdapter,
        ) { todoItem -> deleteItem(todoItem) }.createHelper()
    }

    private fun provideObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listTodoItem.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    updateRecyclerItems(it)
                    binding.swipeLayout.isRefreshing = false
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.countOfComplete.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    binding.countCompleteTextView.text = getString(R.string.complete, it)
                }
        }
    }

    private fun setupListeners() {
        binding.navigateToAddFAB.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_addEditItem)
        }
        with(binding.showUncheckedCheckbox) {
            isChecked = viewModel.showUnchecked
            setOnClickListener {
                viewModel.changeShow()
            }
        }

        binding.swipeLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeLayout.isRefreshing = false
        }

        binding.logout.setOnClickListener {
            viewModel.logOut()
            findNavController().navigate(R.id.action_todoListFragment_to_authFragment)
        }
    }

    private fun updateRecyclerItems(listTodo: List<TodoItem>) {
        if (binding.showUncheckedCheckbox.isChecked) {
            rvAdapter.diffList.submitList(listTodo)
        } else {
            rvAdapter.diffList.submitList(listTodo.filter { item -> !item.isComplete })
        }
    }

    private fun navigateToAddEditFragment(todoItemID: String) {
        findNavController().navigate(
            TodoListFragmentDirections.actionTodoListFragmentToAddEditItem(todoItemID),
        )
    }

    private fun deleteItem(deletedCourse: TodoItem) {
        viewModel.removeTodoItem(deletedCourse)

        Snackbar.make(
            binding.todoListConstraintLayout,
            getString(R.string.deleted) + deletedCourse.text,
            Snackbar.LENGTH_LONG,
        ).setAction(
            getString(R.string.undo),
        ) {
            viewModel.addTodoItem(deletedCourse)
        }.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
