package com.example.jstmcpandroid.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DataDao {
    @Insert
    suspend fun insertData(data: DataEntity)

    @Update
    suspend fun updateData(data: DataEntity)

    @Query("DELETE FROM mcp_table")
    suspend fun deleteAllData()

    @Query("SELECT * FROM mcp_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<DataEntity>>
}