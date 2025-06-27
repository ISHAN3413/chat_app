package com.example.globalchat.model.DAO

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: MessageDatabase
    val messagerepository by lazy{
        MessageRepository(
            messagesDao = database.messagedao()
        )
    }
    fun provide(context: Context){
        database = Room.databaseBuilder(context, MessageDatabase::class.java, "wishlist.db").addMigrations(
         MIGRATION_1_2).build()
    }
}