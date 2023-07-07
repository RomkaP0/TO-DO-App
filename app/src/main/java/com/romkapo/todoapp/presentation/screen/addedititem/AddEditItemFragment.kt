package com.romkapo.todoapp.presentation.screen.addedititem

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.core.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.databinding.FragmentAddEditItemBinding
import com.romkapo.todoapp.utils.Convert
import com.romkapo.todoapp.utils.Importance
import com.romkapo.todoapp.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException
import java.util.UUID
import javax.inject.Inject

class AddEditItemFragment : Fragment() {
    private var _binding: FragmentAddEditItemBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddEditItemFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var addEditFragmentComponent: AddEditFragmentComponent
    private lateinit var viewModel: AddEditItemViewModel

    override fun onAttach(context: Context) {
        addEditFragmentComponent =
            (requireContext().applicationContext as Application).appComponent.addEditFragmentComponentFactory()
                .create()
        addEditFragmentComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this, viewModelFactory)[AddEditItemViewModel::class.java]
        _binding = FragmentAddEditItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isNew = true

        provideObservers()

        try {
            val itemID = args.todoItemID

            viewModel.loadTask(itemID)


            isNew = false
        } catch (e: InvocationTargetException) {
            Log.d("NavArgsException", "There aren`t args")
        }

        with(binding) {
            addEditDateTextView.text = Convert.getDateTime(viewModel.completeTimeStamp)

            addeditButtonClose.setOnClickListener {
                findNavController().navigateUp()
            }
            addeditButtonSave.setOnClickListener {
                tryAddTodoItem(isNew)
                findNavController().navigateUp()
            }

            completeBeforeSwith.setOnCheckedChangeListener { _, isChecked ->
                with(addEditDateTextView) {
                    if (isChecked) {
                        visibility = View.VISIBLE

                        setOnClickListener {
                            buildDatePickerDialog()
                        }
                    } else {
                        visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun provideObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentItemFlow.collect {
                    insertDataFromArgs(it)
                }
            }
        }
    }

    private fun buildDatePickerDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Укажите дату")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(viewModel.completeTimeStamp)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let {
                viewModel.completeTimeStamp = it
                binding.addEditDateTextView.text = Convert.getDateTime(it)
            }
        }
        datePicker.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun tryAddTodoItem(isNew: Boolean) {
        val importance = when (binding.importanceToggleGroup.checkedButtonId) {
            R.id.ImportanceLow -> Importance.LOW
            R.id.ImportanceMiddle -> Importance.MEDIUM
            R.id.ImportanceHigh -> Importance.HIGH
            else -> {
                Importance.MEDIUM
            }
        }

        val dateComplete = if (binding.completeBeforeSwith.isChecked) {
            viewModel.completeTimeStamp
        } else {
            null
        }

        with(viewModel.currentItem) {
            id = viewModel.id
            text = binding.TodoDescribe.text.toString()
            this.importance = importance
            dateCreate = System.currentTimeMillis()
            this.dateComplete = dateComplete

            if (isNew) {
                id = UUID.randomUUID().toString()
                viewModel.addTodoItem(this)
            } else {
                dateEdit = System.currentTimeMillis()
                viewModel.editTodoItem(this)
            }
        }
    }

    private fun insertDataFromArgs(todoItem: TodoItem) {
        viewModel.id = todoItem.id

        with(binding) {
            TodoDescribe.setText(todoItem.text)
            importanceToggleGroup.clearChecked()

            when (todoItem.importance) {
                Importance.LOW -> {
                    importanceToggleGroup.check(R.id.ImportanceLow)
                }

                Importance.HIGH -> {
                    importanceToggleGroup.check(R.id.ImportanceHigh)
                }

                Importance.MEDIUM -> {
                    importanceToggleGroup.check(R.id.ImportanceMiddle)
                }
            }

            todoItem.dateComplete?.let {
                completeBeforeSwith.isChecked = true
                viewModel.completeTimeStamp = it
                binding.addEditDateTextView.apply {
                    text = viewModel.completeTimeStamp.toString()
                    visibility = View.VISIBLE
                }
            }

            addEditDeleteButton.isEnabled = true
            addEditDeleteButton.setOnClickListener {
                viewModel.removeTodoItem(
                    todoItem
                )
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}