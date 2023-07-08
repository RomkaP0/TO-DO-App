package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.romkapo.todoapp.data.model.TodoItem

/* SwipeHelper для удаления элементов в RecyclerView свайпом */

class SwipeRightHelper(
    private val recyclerView: RecyclerView,
    private val adapter: TodoListAdapter,
    private val function: (todoItem: TodoItem) -> Unit
) {
    fun createHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourse: TodoItem =
                    adapter.diffList.currentList[viewHolder.adapterPosition]
                function(deletedCourse)
            }
        }).attachToRecyclerView(recyclerView)
    }
}