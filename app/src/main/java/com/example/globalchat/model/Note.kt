package com.example.globalchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String? = null,
    @SerialName("created_at")
    val createdAt: String? =null,
    val name: String = "",
    val about: String = "",
    val uid: String = ""
)

@Serializable
data class ChatUserMessage(
    val id: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    val sender_id: String = "",
    val receiver_id: String = "",
    val content: String = "",
    val time:String = "",
)

@Serializable
data class conrequest(
    val id: Int? = null,
    @SerialName("created_at")
    val created_at: String? = null,
    val sender_id: String = "",
    val reciever_id: String = "",
    val status : String = ""

)


