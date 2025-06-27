package com.example.globalchat.model.DAO

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [messageDao::class],
    version = 2,
    exportSchema = false
)
abstract class MessageDatabase  : RoomDatabase() {
    abstract fun messagedao(): MessagesDao
}