package com.example.globalchat.model.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
abstract class MessagesDao{
@Insert(onConflict = OnConflictStrategy.REPLACE)
abstract suspend fun addAmessage(messageEntity: messageDao)

@Insert(onConflict = OnConflictStrategy.REPLACE)
abstract suspend fun addlistofmessage(messageEntity: List<messageDao>)

@Query("DELETE FROM 'message_dao' where message_username = :username")
abstract suspend fun deleteallmessages(username: String)

@Query("DELETE FROM 'message_dao' WHERE id IN (:ids)")
abstract fun deleteMessagesByIds(ids: List<Long>)

@Query("Select * from 'message_dao' where message_username =:username")
abstract  fun getallmessages(username:String):Flow<List<messageDao>>

@Query("Select * from 'message_dao' where id =:id")
abstract  fun getmessagebyid(id:String):messageDao

@Query("UPDATE message_dao SET message_isseen = :isSeen WHERE id = :id")
abstract suspend fun updateMessageSeenStatus(id: Long, isSeen: Boolean)
}
