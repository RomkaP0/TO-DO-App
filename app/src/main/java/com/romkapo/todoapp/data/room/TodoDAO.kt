package com.romkapo.todoapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romkapo.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDAO {
    @Query("SELECT * FROM todoItems ORDER BY dateEdit DESC")
    fun getTodoListFlow(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todoItems WHERE id = :id")
    fun getTodoItemById(id: String): TodoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodoList(itemList: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertTodoItem(todo: TodoItem)

    @Delete
    fun deleteTodoItem(todo: TodoItem)

    @Query("DELETE FROM todoItems")
    fun dropTodoItems()
}
