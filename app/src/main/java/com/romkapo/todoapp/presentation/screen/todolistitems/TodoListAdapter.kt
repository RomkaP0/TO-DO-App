package com.romkapo.todoapp.presentation.screen.todolistitems

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romkapo.todoapp.R
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.databinding.TodoItemBinding
import com.romkapo.todoapp.utils.Importance
import com.romkapo.todoapp.utils.LongToString

class TodoListAdapter(
    private val editClickListener: (String) -> Unit,
    private val deleteClickListener: (TodoItem) -> Unit,
    private val checkboxClickListener: (TodoItem) -> Unit,
) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem == newItem
    }

    val diffList = AsyncListDiffer(this, diffCallback)

    inner class TodoListViewHolder(val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder(
            TodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int {
        return diffList.currentList.size
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val currentTodoItem = diffList.currentList[position]

        with(holder.binding) {
            setListeners(binding = this, root.context, currentTodoItem)

            todoTextView.text = currentTodoItem.text
            todoTextView.paint.isStrikeThruText = currentTodoItem.isComplete
            isCheckedTodo.isChecked = currentTodoItem.isComplete
            isCheckedTodo.isErrorShown = false

            setDataVisibility(todoListDate, currentTodoItem.dateCreate)
            setDataVisibility(todoListModifyDate, currentTodoItem.dateEdit)
            setDataVisibility(todoListCompleteDate, currentTodoItem.dateComplete)

            setImportance(currentTodoItem.importance, this@with, root.context)
        }
    }

    private fun setListeners(
        binding: TodoItemBinding,
        context: Context,
        currentTodoItem: TodoItem,
    ) {
        binding.isCheckedTodo.setOnClickListener {
            currentTodoItem.isComplete = binding.isCheckedTodo.isChecked
            currentTodoItem.dateEdit = System.currentTimeMillis()
            checkboxClickListener(currentTodoItem)
            binding.todoTextView.paint.isStrikeThruText = binding.isCheckedTodo.isChecked
            binding.todoTextView.invalidate()
        }
        binding.todoItemLayout.setOnClickListener { editClickListener(currentTodoItem.id) }
        binding.todoItemLayout.setOnLongClickListener {
            showMenu(currentTodoItem, context, binding.todoItemLayout)
            return@setOnLongClickListener true
        }
    }

    private fun setDataVisibility(textView: TextView, date: Long?) {
        if (date != null) {
            textView.text = LongToString.getDateTime(date)
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    private fun setImportance(importance: Importance, binding: TodoItemBinding, context: Context) {
        binding.importanceIcon.visibility = View.VISIBLE
        when (importance) {
            Importance.LOW -> {
                binding.importanceIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_arrow_down),
                )
            }

            Importance.HIGH -> {
                binding.importanceIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_warning),
                )
                binding.isCheckedTodo.isErrorShown = true
            }

            else -> binding.importanceIcon.visibility = View.GONE
        }
    }

    private fun showMenu(todoItem: TodoItem, context: Context, layout: View) {
        val popup = PopupMenu(context, layout)
        popup.menuInflater.inflate(R.menu.dropdown_item_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.itemEdit -> {
                    editClickListener(todoItem.id)
                }

                R.id.itemDelete -> {
                    deleteClickListener(todoItem)
                }
            }
            return@setOnMenuItemClickListener false
        }
        popup.show()
    }
}
