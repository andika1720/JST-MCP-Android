package com.example.jstmcpandroid.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mcp_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kategori: String,
    val nilai1: Int,
    val nilai2: Int,
    val nilai3: Int,
    val nilai4: Int
)