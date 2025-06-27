package com.example.globalchat.model.DAO

import android.app.Application
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class GlobalChatApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE message_dao ADD COLUMN message_isseen INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE message_dao ADD COLUMN message_isforwarded INTEGER NOT NULL DEFAULT 0")
    }


}
