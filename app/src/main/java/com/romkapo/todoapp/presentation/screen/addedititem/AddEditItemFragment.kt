@file:OptIn(ExperimentalMaterial3Api::class)

package com.romkapo.todoapp.presentation.screen.addedititem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.WhiteRoundColumn
import com.romkapo.todoapp.utils.LongToString

//class AddEditItemFragment : Fragment() {
//    private var _binding: FragmentAddEditItemBinding? = null
//    private val binding get() = _binding!!
//    private val args by navArgs<AddEditItemFragmentArgs>()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    private lateinit var addEditFragmentComponent: AddEditFragmentComponent
//    private val viewModel: AddEditItemViewModel by viewModels { viewModelFactory }
//
//    override fun onAttach(context: Context) {
//        addEditFragmentComponent =
//            (requireContext().applicationContext as Application).appComponent.addEditFragmentComponentFactory()
//                .create()
//        addEditFragmentComponent.inject(this)
//        super.onAttach(context)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        _binding = FragmentAddEditItemBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        lifecycleScope.launch {
//            viewModel.currentItemFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect {
//                    viewModel.currentItem = it
//                    enableDeleteButton(it)
//                    insertDataFromArgs(it)
//                }
//        }
//
//        try {
//            viewModel.loadTask(args.todoItemID)
//        } catch (e: InvocationTargetException) {
//            Log.d("NavArgsException", "There aren`t args")
//        }
//
//        binding.addEditDateTextView.text = LongToString.getDateTime(
//            viewModel.currentItem.dateComplete ?: System.currentTimeMillis(),
//        )
//        setupListeners()
//    }
//
//    private fun setupListeners() {
//        binding.addEditButtonClose.setOnClickListener {
//            findNavController().navigateUp()
//        }
//
//        binding.addEditButtonSave.setOnClickListener {
//            tryAddTodoItem()
//            findNavController().navigateUp()
//        }
//
//        binding.completeBeforeSwith.setOnCheckedChangeListener { _, isChecked ->
//            provideCheckChangeLogic(isChecked)
//        }
//    }
//
//    private fun provideCheckChangeLogic(isChecked: Boolean) {
//        val time = viewModel.currentItem.dateComplete ?: System.currentTimeMillis()
//        viewModel.currentItem.dateComplete = time
//        with(binding.addEditDateTextView) {
//            text = LongToString.getDateTime(time)
//            if (isChecked) {
//                visibility = View.VISIBLE
//                setOnClickListener { buildDatePickerDialog(time) }
//            } else {
//                visibility = View.INVISIBLE
//            }
//        }
//    }
//
//    private fun buildDatePickerDialog(time: Long) {
//        val datePicker = MaterialDatePicker.Builder.datePicker()
//            .setTitleText(getString(R.string.choose_data))
//            .setTheme(R.style.ThemeOverlay_App_DatePicker)
//            .setSelection(time)
//            .build()
//
//        datePicker.addOnPositiveButtonClickListener {
//            datePicker.selection?.let {
//                viewModel.currentItem.dateComplete = it
//                binding.addEditDateTextView.text = LongToString.getDateTime(it)
//            }
//        }
//        datePicker.show(requireActivity().supportFragmentManager, "tag")
//    }
//
//    private fun tryAddTodoItem() {
//        with(viewModel.currentItem) {
//            importance = when (binding.importanceToggleGroup.checkedButtonId) {
//                R.id.ImportanceLow -> Importance.LOW
//                R.id.ImportanceMiddle -> Importance.MEDIUM
//                R.id.ImportanceHigh -> Importance.HIGH
//                else -> Importance.MEDIUM
//            }
//            text = binding.TodoDescribe.text.toString()
//            dateCreate = if (dateCreate == 0L) System.currentTimeMillis() else dateCreate
//            dateComplete = if (binding.completeBeforeSwith.isChecked) dateComplete else null
//            if (id == "-1") {
//                id = UUID.randomUUID().toString()
//                viewModel.addTodoItem(this)
//            } else {
//                dateEdit = System.currentTimeMillis()
//                viewModel.editTodoItem(this)
//            }
//        }
//    }
//
//    private fun insertDataFromArgs(todoItem: TodoItem) {
//        binding.TodoDescribe.setText(todoItem.text)
//        binding.importanceToggleGroup.clearChecked()
//
//        when (todoItem.importance) {
//            Importance.LOW -> binding.importanceToggleGroup.check(R.id.ImportanceLow)
//            Importance.HIGH -> binding.importanceToggleGroup.check(R.id.ImportanceHigh)
//            Importance.MEDIUM -> binding.importanceToggleGroup.check(R.id.ImportanceMiddle)
//        }
//
//        todoItem.dateComplete?.let {
//            binding.completeBeforeSwith.isChecked = true
//            binding.addEditDateTextView.apply {
//                text = LongToString.getDateTime(it).toString()
//                visibility = View.VISIBLE
//            }
//        }
//        binding.addEditButtonDelete.isEnabled = true
//    }
//
//    private fun enableDeleteButton(todoItem: TodoItem) {
//        binding.addEditButtonDelete.setOnClickListener {
//            viewModel.removeTodoItem(todoItem)
//            findNavController().navigateUp()
//        }
//    }
//
//    override fun onDestroyView() {
//        _binding = null
//        super.onDestroyView()
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun AddEditScreen(
    viewModel: AddEditItemViewModel = daggerViewModel(),
    navigateUp: () -> Boolean,
    navToTodoList: () -> Unit
) {
    val state = viewModel.currentItemFlow.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val textColor = animateColorAsState(
        targetValue = if (state.value.isHighlight) Color.Red else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 1000)
    )


    val provideWidthModifier = Modifier.fillMaxWidth()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {

        Row(modifier = provideWidthModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                    contentDescription = null
                )
            }
            TextButton(onClick = { 
                viewModel.saveTodoItem()
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        }
        OutlinedTextField(
            modifier = provideWidthModifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                )
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            ),
            minLines = 3,
            maxLines = 3,
            placeholder = { Text(text = "Название события...") },
            value = state.value.text,
            onValueChange = { viewModel.changeText(it) })

        WhiteRoundColumn(modifier = provideWidthModifier) {
            Row(
                modifier = provideWidthModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.importance))
                TextButton(onClick = {
                    viewModel.changeBottomSheetState()
                }) {
                    Text(text = state.value.importance, color = textColor.value)
                }
            }
            Divider(modifier = provideWidthModifier.padding(horizontal = 4.dp))
            Row(
                modifier = provideWidthModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.complete_before))
                Switch(
                    checked = state.value.hasDeadline,
                    onCheckedChange = { viewModel.changeStateDeadline() })
            }
            if (state.value.hasDeadline) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = LongToString.getDateTime(state.value.deadline))
                }
            }
        }
        TextButton(
            modifier = provideWidthModifier.background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp)
            ),
            enabled = !state.value.isNew,
            onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.delete))
        }
    }
    if (state.value.isBottomSheetOpened) {
        ModalSheetImportance(
            sheetState = bottomSheetState,
            dismiss = { viewModel.changeBottomSheetState() },
            onItemClick = { value -> viewModel.changeImportance(value) })
    }
}