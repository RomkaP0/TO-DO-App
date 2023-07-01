package com.romkapo.todoapp.presentation.screen.todoaddedititem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.romkapo.todoapp.R
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.network.ConnectionObserver
import com.romkapo.todoapp.databinding.FragmentAddEditItemBinding
import com.romkapo.todoapp.utils.Convert
import com.romkapo.todoapp.utils.Importance
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException
import java.util.UUID

@AndroidEntryPoint
class AddEditItemFragment : Fragment() {
    private var _binding: FragmentAddEditItemBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddEditItemFragmentArgs>()
    private val viewModel: AddEditItemViewModel by viewModels()
    private var internetState = ConnectionObserver.Status.Unavailable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isNew = true

        try {
            val itemID = args.todoItemID

            viewModel.loadTask(itemID)

            lifecycleScope.launch {
                viewModel.currentItemFlow.collect {
                    insertDataFromArgs(it)
                }
            }
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
                tryRemote({viewModel.createRemoteTask(this)}, R.string.no_internet_connection)
                viewModel.addTodoItemAt(this)
            } else {
                dateEdit = System.currentTimeMillis()
                tryRemote({viewModel.updateRemoteTask(this)}, R.string.no_internet_connection)
                viewModel.updateTask(this)
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
            }

            addEditDeleteButton.isEnabled = true
            addEditDeleteButton.setOnClickListener {
                tryRemote({viewModel.deleteRemoteTask(viewModel.id)}, R.string.no_internet_connection)
                viewModel.removeItemWithID(viewModel.id)
                findNavController().navigateUp()
            }

            if (todoItem.dateComplete != null) {
                completeBeforeSwith.isChecked = true
            }
        }
    }

    private fun tryRemote(action: () -> Unit, stringID: Int) {
        if (internetState == ConnectionObserver.Status.Available) {
            action()
        } else {
            provideSnackBar(
                stringID,
            )
        }
    }

    private fun provideSnackBar(stringID: Int) {
        Snackbar.make(
            binding.addEditLayout,
            getString(stringID),
            Snackbar.LENGTH_SHORT
        ).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}