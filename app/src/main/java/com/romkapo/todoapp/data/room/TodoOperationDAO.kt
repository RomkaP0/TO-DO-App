package com.romkapo.todoapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romkapo.todoapp.data.model.network.UnSyncTodoItem

/* DAO операций для таблицы несинхронизированных данных*/
@Dao
interface TodoOperationDAO {

    @Query("SELECT * FROM unsyncoperations ORDER BY timestamp ASC")
    fun getUnSyncTodoList(): List<UnSyncTodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnSyncOperation(unSyncTodoItem: UnSyncTodoItem)

    @Query("DELETE FROM unsyncoperations WHERE id = :id")
    fun deleteOperationWithId(id: String)

    @Query("DELETE FROM unsyncoperations")
    fun dropTodoItems()
}
