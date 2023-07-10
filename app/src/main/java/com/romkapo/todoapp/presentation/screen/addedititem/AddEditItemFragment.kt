package com.romkapo.todoapp.presentation.screen.addedititem

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.databinding.FragmentAddEditItemBinding
import com.romkapo.todoapp.di.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.utils.Importance
import com.romkapo.todoapp.utils.LongToString
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException
import java.util.UUID
import javax.inject.Inject

class AddEditItemFragment : Fragment() {
    private var _binding: FragmentAddEditItemBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddEditItemFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var addEditFragmentComponent: AddEditFragmentComponent
    private val viewModel: AddEditItemViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        addEditFragmentComponent =
            (requireContext().applicationContext as Application).appComponent.addEditFragmentComponentFactory()
                .create()
        addEditFragmentComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.currentItemFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    viewModel.currentItem = it
                    enableDeleteButton(it)
                    insertDataFromArgs(it)
                }
        }

        try {
            viewModel.loadTask(args.todoItemID)
        } catch (e: InvocationTargetException) {
            Log.d("NavArgsException", "There aren`t args")
        }

        binding.addEditDateTextView.text = LongToString.getDateTime(
            viewModel.currentItem.dateComplete ?: System.currentTimeMillis(),
        )
        setupListeners()
    }

    private fun setupListeners() {
        binding.addEditButtonClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addEditButtonSave.setOnClickListener {
            tryAddTodoItem()
            findNavController().navigateUp()
        }

        binding.completeBeforeSwith.setOnCheckedChangeListener { _, isChecked ->
            provideCheckChangeLogic(isChecked)
        }
    }

    private fun provideCheckChangeLogic(isChecked: Boolean) {
        val time = viewModel.currentItem.dateComplete ?: System.currentTimeMillis()
        viewModel.currentItem.dateComplete = time
        with(binding.addEditDateTextView) {
            text = LongToString.getDateTime(time)
            if (isChecked) {
                visibility = View.VISIBLE
                setOnClickListener { buildDatePickerDialog(time) }
            } else {
                visibility = View.INVISIBLE
            }
        }
    }

    private fun buildDatePickerDialog(time: Long) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.choose_data))
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(time)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let {
                viewModel.currentItem.dateComplete = it
                binding.addEditDateTextView.text = LongToString.getDateTime(it)
            }
        }
        datePicker.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun tryAddTodoItem() {
        with(viewModel.currentItem) {
            importance = when (binding.importanceToggleGroup.checkedButtonId) {
                R.id.ImportanceLow -> Importance.LOW
                R.id.ImportanceMiddle -> Importance.MEDIUM
                R.id.ImportanceHigh -> Importance.HIGH
                else -> Importance.MEDIUM }
            text = binding.TodoDescribe.text.toString()
            dateCreate = if (dateCreate == 0L) System.currentTimeMillis() else dateCreate
            dateComplete = if (binding.completeBeforeSwith.isChecked) dateComplete else null
            if (id == "-1") {
                id = UUID.randomUUID().toString()
                viewModel.addTodoItem(this)
            } else {
                dateEdit = System.currentTimeMillis()
                viewModel.editTodoItem(this)
            }
        }
    }

    private fun insertDataFromArgs(todoItem: TodoItem) {
        binding.TodoDescribe.setText(todoItem.text)
        binding.importanceToggleGroup.clearChecked()

        when (todoItem.importance) {
            Importance.LOW -> binding.importanceToggleGroup.check(R.id.ImportanceLow)
            Importance.HIGH -> binding.importanceToggleGroup.check(R.id.ImportanceHigh)
            Importance.MEDIUM -> binding.importanceToggleGroup.check(R.id.ImportanceMiddle)
        }

        todoItem.dateComplete?.let {
            binding.completeBeforeSwith.isChecked = true
            binding.addEditDateTextView.apply {
                text = LongToString.getDateTime(it).toString()
                visibility = View.VISIBLE
            }
        }
        binding.addEditButtonDelete.isEnabled = true
    }

    private fun enableDeleteButton(todoItem: TodoItem) {
        binding.addEditButtonDelete.setOnClickListener {
            viewModel.removeTodoItem(todoItem)
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
