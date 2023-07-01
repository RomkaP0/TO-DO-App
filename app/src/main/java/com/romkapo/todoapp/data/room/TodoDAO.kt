package com.romkapo.todoapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romkapo.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDAO {
    @Query("SELECT * FROM todoItems")
    fun getTodoListFlow(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todoItems")
    fun getTodoList():List<TodoItem>

    @Query("SELECT * FROM todoItems WHERE isComplete=:state")
    fun getUncheckedTodoListFlow(state: Boolean=false): Flow<List<TodoItem>>

    @Query("SELECT * FROM todoItems WHERE id = :id")
    fun getTodoItemById(id: String): TodoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodoList(itemList: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodoItem(todo: TodoItem)

    @Query("DELETE FROM todoItems WHERE id = :id")
    fun deleteTodoItemById(id: String)

    @Query("UPDATE todoItems SET isComplete = :isComplete, dateEdit = :dateEdit WHERE id = :id")
    fun setTodoItemState(id: String, isComplete: Boolean, dateEdit:Long)
}