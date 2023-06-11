package com.romka_po.to_doapp.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romka_po.to_doapp.databinding.TodoItemBinding


class TodoListAdapter:
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<TodoItem>() {

        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem == newItem
    }

    inner class TodoListViewHolder(val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    val diffList = AsyncListDiffer(this, diffCallback)


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
        with(holder.binding){
            isCheckedTodo.isChecked = currentTodoItem.isComplete
            todoTextView.text = currentTodoItem.text
        }
    }
}