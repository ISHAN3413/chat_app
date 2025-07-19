package com.example.globalchat.model

sealed class UserState{
    object loggedin:UserState()
    object Loading :UserState()
    object authenticated:UserState()
    object unauthenticated:UserState()
    object profilefilled:UserState()
    object profileupdated:UserState()
    object success: UserState()
    data class Error(val message: String):UserState()
}
sealed class UserData{
    data class profile(val name: String,val about: String):UserData()
}
