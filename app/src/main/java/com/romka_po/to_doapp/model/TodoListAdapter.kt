package com.romka_po.to_doapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romka_po.to_doapp.R
import com.romka_po.to_doapp.databinding.TodoItemBinding
import com.romka_po.to_doapp.utils.Convert
import com.romka_po.to_doapp.utils.Importance


class TodoListAdapter(
    private val shortClickListener: (TodoItem) -> Unit,
    private val longClickListener: (TodoItem, Int) -> Unit,
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
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return diffList.currentList.size
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val currentTodoItem = diffList.currentList[position]

        with(holder.binding) {
            with(isCheckedTodo) {
                todoTextView.text = currentTodoItem.text

                isChecked = currentTodoItem.isComplete
                isErrorShown = false

                setOnClickListener {
                    if (isChecked) {
                        currentTodoItem.isComplete = true
                        currentTodoItem.dateComplete = System.currentTimeMillis()
                        checkboxClickListener(currentTodoItem)
                        todoListDate.visibility = View.VISIBLE
                    } else {
                        currentTodoItem.isComplete = false
                        currentTodoItem.dateComplete = null
                        checkboxClickListener(currentTodoItem)
                        todoListDate.visibility = View.GONE
                    }
                    todoListDate.text = currentTodoItem.dateComplete?.let { it1 ->
                        Convert.getDateTime(
                            it1
                        )
                    }
                }
            }

            importanceIcon.visibility = View.VISIBLE

            with(todoListDate) {
                if (currentTodoItem.dateComplete != null) {
                    text = Convert.getDateTime(currentTodoItem.dateComplete!!)
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }

            with(todoListModifyDate) {
                if (currentTodoItem.dateEdit != null) {
                    text = Convert.getDateTime(currentTodoItem.dateEdit!!)
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }

            when (currentTodoItem.importance) {
                Importance.LOW -> {
                    importanceIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                }

                Importance.HIGH -> {
                    importanceIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context,
                            R.drawable.ic_warning
                        )
                    )
                    isCheckedTodo.isErrorShown = true
                }

                else -> {
                    importanceIcon.visibility = View.GONE
                }
            }

            todoItemLayout.setOnClickListener { shortClickListener(currentTodoItem) }
            todoItemLayout.setOnLongClickListener {
                longClickListener(currentTodoItem, position)
                return@setOnLongClickListener true
            }
        }
    }
}
