@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.EyeCheckBox
import com.romkapo.todoapp.ui.common.TodoListItem
import com.romkapo.todoapp.ui.theme.Typography

//class TodoListFragment : Fragment() {
//    private lateinit var rvAdapter: TodoListAdapter
//
//    private var _binding: FragmentTodoListBinding? = null
//    private val binding get() = _binding!!
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    private lateinit var todoFragmentComponent: TodoListItemFragmentComponent
//    private val viewModel: TodoItemListViewModel by viewModels { viewModelFactory }
//
//    override fun onAttach(context: Context) {
//        todoFragmentComponent = (requireContext().applicationContext as Application)
//            .appComponent.todoItemListFragmentComponentFactory().create()
//        todoFragmentComponent.inject(this)
//        super.onAttach(context)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.swipeLayout.isRefreshing = false
//
//        setupRecyclerView(binding.todoRecyclerView)
//
//        provideObservers()
//        viewModel.getAllList()
//        setupListeners()
//    }
//
//    private fun setupRecyclerView(recyclerView: RecyclerView) {
//        rvAdapter = TodoListAdapter(
//            editClickListener = { id -> navigateToAddEditFragment(id) },
//            deleteClickListener = { todoItem -> deleteItem(todoItem) },
//            checkboxClickListener = { todoItem -> viewModel.editStateTodoItem(todoItem) },
//        )
//
//        with(recyclerView) {
//            adapter = rvAdapter
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, VERTICAL))
//        }
//        SwipeRightHelper(
//            recyclerView,
//            rvAdapter,
//        ) { todoItem -> deleteItem(todoItem) }.createHelper()
//    }
//
//    private fun provideObservers() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.listTodoItem.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collectLatest {
//                    updateRecyclerItems(it)
//                    binding.swipeLayout.isRefreshing = false
//                }
//        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.countOfComplete.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collectLatest {
//                    binding.countCompleteTextView.text = getString(R.string.complete, it)
//                }
//        }
//    }
//
//    private fun setupListeners() {
//        binding.navigateToAddFAB.setOnClickListener {
//            findNavController().navigate(R.id.action_todoListFragment_to_addEditItem)
//        }
//        with(binding.showUncheckedCheckbox) {
//            isChecked = viewModel.showUnchecked
//            setOnClickListener {
//                viewModel.changeShow()
//            }
//        }
//
//        binding.swipeLayout.setOnRefreshListener {
//            viewModel.refresh()
//            binding.swipeLayout.isRefreshing = false
//        }
//
//        binding.logout.setOnClickListener {
//            viewModel.logOut()
//            findNavController().navigate(R.id.action_todoListFragment_to_authFragment)
//        }
//    }
//
//    private fun updateRecyclerItems(listTodo: List<TodoItem>) {
//        if (binding.showUncheckedCheckbox.isChecked) {
//            rvAdapter.diffList.submitList(listTodo)
//        } else {
//            rvAdapter.diffList.submitList(listTodo.filter { item -> !item.isComplete })
//        }
//    }
//
//    private fun navigateToAddEditFragment(todoItemID: String) {
//        findNavController().navigate(
//            TodoListFragmentDirections.actionTodoListFragmentToAddEditItem(todoItemID),
//        )
//    }
//
//    private fun deleteItem(deletedCourse: TodoItem) {
//        viewModel.removeTodoItem(deletedCourse)
//
//        Snackbar.make(
//            binding.todoListConstraintLayout,
//            getString(R.string.deleted) + deletedCourse.text,
//            Snackbar.LENGTH_LONG,
//        ).setAction(
//            getString(R.string.undo),
//        ) {
//            viewModel.addTodoItem(deletedCourse)
//        }.show()
//    }
//
//}

@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoItemListViewModel = daggerViewModel(),
) {
    val state = viewModel.sampleData.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                onClick = { navController.navigate("add_edit") })
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.my_tasks), style = Typography.titleLarge)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.complete) + " " + state.value.countOfCompleted)
                EyeCheckBox(isChecked = state.value.isCheckedShown) {
                    viewModel.changeShow()
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(24.dp)),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top)
            ) {
                items(items = state.value.todoList) { item ->
                    val dismissState = rememberDismissState(confirmValueChange = {
                        viewModel.removeTodoItem(item)
                        true
                    })
                    SwipeToDismiss(modifier = Modifier.animateItemPlacement(), state = dismissState, background = {}, dismissContent = {
                        TodoListItem(item,
                            onCheckChanged = {
                                viewModel.editStateTodoItem(item.copy(isComplete = !item.isComplete))
                            },
                            onItemClicked = {
                                navController.navigate("add_edit?id=${item.id}")
                            })
                    })
                }
            }
        }
    }
}