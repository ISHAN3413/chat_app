package com.example.globalchat.model.DAO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "message_dao")
data class messageDao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "message_textMessage")
    val textMessage: String="",
    @ColumnInfo(name = "message_username")
    val username: String="",
    @ColumnInfo(name = "message_time")
    val time: Long = 0L,
    @ColumnInfo(name = "message_isme")
    val isme: Boolean,
    @ColumnInfo(name = "message_isseen")
    val isseen: Boolean,
    @ColumnInfo(name = "message_isforwarded")
    val isforwarded: String=""
)