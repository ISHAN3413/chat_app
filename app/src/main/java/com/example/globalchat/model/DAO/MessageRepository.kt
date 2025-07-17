package com.example.globalchat.model.DAO

import com.example.globalchat.view.HomeLayouts.Message
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messagesDao: MessagesDao) {

    suspend fun addAmessage(messageDao: messageDao){
        messagesDao.addAmessage(messageDao)
    }
    suspend fun addlistofmessage(messageDao: List<messageDao>){
        messagesDao.addlistofmessage(messageDao)
    }
    suspend fun deleteAllmessage(username: String){
        messagesDao.deleteallmessages(username)
    }
    suspend fun updateseenstatus(id:Long , istrue:Boolean){
        messagesDao.updateMessageSeenStatus(id,istrue)
    }
    suspend fun updatemessageTextMessage(id:String , textmessage:String){
        messagesDao.updateMessageTextMessage(id,textmessage)
    }
    suspend fun deletemessagesByid(ids:List<Long>){
        messagesDao.deleteMessagesByIds(ids)
    }
    fun getWishes(username:String): Flow<List<messageDao>> = messagesDao.getallmessages(username)
    fun getMessagebyid(id:String): messageDao = messagesDao.getmessagebyid(id)

}