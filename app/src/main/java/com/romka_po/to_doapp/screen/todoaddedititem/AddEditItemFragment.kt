package com.romka_po.to_doapp.screen.todoaddedititem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.romka_po.to_doapp.R
import com.romka_po.to_doapp.databinding.FragmentAddEditItemBinding
import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.utils.Convert
import com.romka_po.to_doapp.utils.Importance
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.InvocationTargetException

@AndroidEntryPoint
class AddEditItemFragment : Fragment() {
    private var _binding: FragmentAddEditItemBinding? = null
    private var id = ""
    private val binding get() = _binding!!
    private val args by navArgs<AddEditItemFragmentArgs>()
    private val viewModel: AddEditItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddEditItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isNew = true

        try {
            insertDataFromArgs(args.todoItem)
            isNew = false
        } catch (e: InvocationTargetException) {
            Log.d("NavArgsException", "There aren`t args")
        }

        binding.addEditDateTextView.text = Convert.getDateTime(viewModel.completeTimeStamp)

        binding.addeditButtonClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addeditButtonSave.setOnClickListener {
            tryAddTodoItem(isNew)
            findNavController().navigate(R.id.action_addEditItem_to_todoListFragment)
        }

        binding.completeBeforeSwith.setOnCheckedChangeListener { _, isChecked ->
            with(binding.addEditDateTextView) {
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

        val item = TodoItem(
            id,
            binding.TodoDescribe.text.toString(),
            importance,
            System.currentTimeMillis(),
            dateComplete
        )
        if (isNew) {
            item.id = item.hashCode().toString()
            viewModel.addTodoItem(item)
        } else {
            item.dateEdit = System.currentTimeMillis()
            item.dateCreate = args.todoItem.dateCreate
            viewModel.editTodoItem(item)
        }
    }

    private fun insertDataFromArgs(todoItem: TodoItem) {
        id = todoItem.id
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
                viewModel.removeItemWithID(id)
                findNavController().navigate(R.id.action_addEditItem_to_todoListFragment)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}