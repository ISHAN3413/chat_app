package com.example.globalchat.ViewModels

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalchat.BuildConfig
import com.example.globalchat.ViewModels.SupabaseClient.client
import com.example.globalchat.model.ChatUserMessage
import com.example.globalchat.model.DAO.Graph
import com.example.globalchat.model.DAO.MessageRepository
import com.example.globalchat.model.DAO.messageDao
import com.example.globalchat.model.Note
import com.example.globalchat.model.UserData
import com.example.globalchat.model.UserState
import com.example.globalchat.view.HomeLayouts.Message
import com.google.ai.client.generativeai.GenerativeModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.createChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.globalchat.model.conrequest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ){
        install(GoTrue)
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}
class AuthViewModel (
    private val messageRepository: MessageRepository = Graph.messagerepository
):ViewModel() {
    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _userState
    private val _userdata = MutableLiveData<UserData>()
    val userdata: LiveData<UserData> = _userdata
    private val _instruments = MutableLiveData<List<Note>>()
    val instruments: LiveData<List<Note>> = _instruments
    private val _conrequests = MutableLiveData<List<conrequest>>()
    val conrequest: LiveData<List<conrequest>> = _conrequests
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> = _userId
    private val _messages = mutableStateListOf<Message>()
    lateinit var getAllMessage: Flow<List<messageDao>>
    lateinit var getAllforlastMessage: Flow<List<messageDao>>
    val messages :SnapshotStateList<Message> get() = _messages
    val generatieModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = BuildConfig.GEMINI_KEY
    )
    private val chat = generatieModel.startChat()
    var selectedids = mutableStateListOf<Long>()
    var count = mutableStateOf("")

    init {
        checkauthentication()
    }
    fun checkauthentication() {
        viewModelScope.launch {
            val session = client.gotrue.currentSessionOrNull()
            _userId.value = session?.user?.id ?: ""
            _userState.value =
                if (session != null) UserState.authenticated else UserState.unauthenticated
        }
    }
     fun deleteallmessages(receiver_id: String){
         val session = client.gotrue.currentSessionOrNull()
         val userId = session?.user?.id
        viewModelScope.launch {
            Log.d("spec1", "")
            messageRepository.deleteAllmessage("${receiver_id}and${userId}")
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun loadingmessagefromRoom(receiver_id: String, senderName: String) {
        _messages.clear()
        val session = client.gotrue.currentSessionOrNull()
        val userId = session?.user?.id
        if (userId != null) {
            loadAllMessages(receiver_id,userId,senderName)
        }
        viewModelScope.launch {
                getAllMessage = messageRepository.getWishes("${receiver_id}and${userId}")
                getAllMessage.collect { message ->
                    Log.d("spec1","${receiver_id}and${userId}" )
                    val realmessage = message.map { it.toMessage(sender = senderName) }
                    clearmessage()
                    _messages.addAll(realmessage)
                }
            }
    }
    fun loadlastmessage(username : String,onlastmessage : (lastmessage:Message)->Unit){
        val session = client.gotrue.currentSessionOrNull()
        val userId = session?.user?.id
        viewModelScope.launch {
            messageRepository.getWishes("${username}and${userId}").collect{
                message ->
                val realmessage = message.map {
                    it.toMessage(sender = username)
                }
               var lastmessage =realmessage.lastOrNull()
                if (lastmessage != null) {
                    onlastmessage(lastmessage)
                }
                else{
                    onlastmessage(Message(
                        textMessage = "",
                        username = "",
                        time = 0L,
                        isme = false,
                        id = 0,
                        isseen = false
                    ))
                }
            }
        }
    }

     fun deletemessagebyid(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                Log.d("deletion",selectedids.toList().toString())
                messageRepository.deletemessagesByid(selectedids.toList())
                selectedids.clear()
            }
            catch (e:Exception){
                Log.e("deletion",e.message.toString())
            }
        }
    }

    fun addmessage(message:Message){
        _messages.add(message)
    }
    fun signUp(userEmail: String, userPassword: String) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                client.gotrue.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                checkauthentication()
            } catch (e: Exception) {
                _userState.value = UserState.Error("${e.message}")
            }
        }
    }
    fun Login(userEmail: String, userPassword: String) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                client.gotrue.loginWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                checkauthentication()
                _userState.value = UserState.authenticated
            } catch (e: Exception) {
                _userState.value = UserState.Error("${e.message}")
            }
        }
    }
    fun LogOut() {
        viewModelScope.launch {
            try {
                client.gotrue.logout()
                _userState.value = UserState.unauthenticated
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Logout failed")
            }
        }
    }

    fun ForgetPassword(email: String) {
        viewModelScope.launch {
            try {
                client.gotrue.sendRecoveryEmail(email)
            } catch (e: Exception) {
                _userState.value = UserState.Error("${e.message}")
            }
        }
    }

    fun createBucket(name: String) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                client.storage.createBucket(id = name) {
                    public = false
                    fileSizeLimit = 10.megabytes
                }
                _userState.value = UserState.profilefilled
            } catch (e: Exception) {
                _userState.value = UserState.Error("${e.message}")
            }
        }
    }

    fun uploadFile(bucketName: String, fileName: String, byteArray: ByteArray) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull()
                val userId = session?.user?.id ?: throw Exception("User not authenticated")
                val fileName = "$userId$fileName.jpg"
                val bucket = client.storage[bucketName]
                bucket.upload(fileName, byteArray, upsert = true)
                _userState.value = UserState.profilefilled
            } catch (e: Exception) {
                _userState.value = UserState.Error("${e.message}")
            }
        }
    }

    fun readFile(
        bucketName: String,
        fileName: String,
        userid: String,
        onImageUrlRetrieved: (url: String) -> Unit
    ) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull()
                val userId = if (userid == "") session?.user?.id
                    ?: throw Exception("User not authenticated") else userid
                val filename = "$userId$fileName.jpg"
                val bucket = client.storage[bucketName]
                val url = bucket.publicUrl("${filename}")
                onImageUrlRetrieved(url)
                _userState.value = UserState.profilefilled
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
    fun saveNote(name: String, about: String) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull()
                    ?: throw Exception("User not authenticated")
                val uid = session.user?.id.toString()
                client.postgrest["test"].insert(
                    Note(
                        name = name,
                        uid = uid,
                        about = about
                    )
                )
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun fetchNotes() {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull()
                    ?: throw Exception("User not authenticated")
                val uid = session.user?.id ?: throw Exception("User ID is null")
                val data = client.postgrest.from("test")
                    .select(
                        columns = Columns.list("id,created_at,name,uid,about"),
                        head = false,
                        filter = {
                            eq("uid", uid)
                        }
                    )
                    .decodeSingle<Note>()
                _userdata.value = UserData.profile(name = data.name, about = data.about)
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error fetching notes: ${e.message}")
            }
        }
    }

    fun updateData(
        bucketName: String,
        fileName: String,
        byteArray: ByteArray,
        name: String,
        about: String
    ) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull()
                val userId = session?.user?.id ?: throw Exception("User not authenticated")
                val fileName = "$userId$fileName.jpg"
                val bucket = client.storage[bucketName]
                bucket.upload(fileName, byteArray, upsert = true)
                client.postgrest["test"].update(
                    {
                        set("name", name)
                        set("about", about)
                    }
                ) {
                    eq("uid", userId)
                }
                _userState.value = UserState.profileupdated
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error fetching notes: ${e.message}")
            }
        }
    }

    fun sendMessagetoGemini(question: String, onreply: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = chat.sendMessage(question)
                val aireply = response.text ?: "Try again later"
                onreply(aireply)
                Log.i("Response from AI", response.text.toString() )
            } catch (e: Exception) {
                Log.e("Response from AI", "Error sending message: ${e.localizedMessage}")
            }
        }
    }

    fun fetchInstruments() {
        viewModelScope.launch {
            try {
                val data = client.postgrest
                    .from("test")
                    .select()
                    .decodeList<Note>()
                _instruments.postValue(data)
            } catch (e: Exception) {
                Log.e("InstrumentsError", "Error fetching instruments: ${e.localizedMessage}")
            }
        }
    }
    fun fetchrequests(){
        viewModelScope.launch{
            try{
                val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
                val uid = session.user?.id ?: throw Exception("No user ID found")
                val data = client.postgrest
                    .from("request table")
                    .select(
                        columns = Columns.list("id,created_at,sender_id,reciever_id,status"),
                        head = false,
                        filter = {
                            and{
                                eq("reciever_id" , "$uid")
                                eq("status" , "Pending")
                            }


                        }
                    )
                    .decodeList<conrequest>()
                _conrequests.postValue(data)

            }catch(e:Exception){
                _userState.value = UserState.Error(e.message.toString())
            }
        }
    }

    private var isRealtimeInitialized = false
    private lateinit var messageChannel: RealtimeChannel
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    fun initRealtimeMessaging(scope: CoroutineScope,sender:String , senderName:String) {
        if (isRealtimeInitialized) return
        val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
        val uid = session.user?.id ?: throw Exception("No user ID found")
        viewModelScope.launch {
            Log.d("spec1", "Connecting...")
            try {
                client.realtime.connect()
                Log.d("spec1", "Connected!")
                messageChannel = client.realtime.createChannel("messages")
                Log.d("spec1", "Channel created")
                messageChannel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                    table = "messages"
                }.onEach {action->
                            try{
                                val message = json.decodeFromJsonElement<ChatUserMessage>(action.record)
                                Log.d("spec1", "${message.content}")
                                if(message.sender_id ==sender && message.receiver_id == uid) {
                                    addmessage(
                                        message = Message(
                                            textMessage = message.content,
                                            username = senderName,
                                            time = System.currentTimeMillis(),
                                            isme = false,
                                            id = 0,
                                            isseen = false,
                                        )
                                    )
                                }
                                Log.d("spec1","${message.sender_id}and${uid}")
                                addingmessagetoRoom(
                                    message = ChatUserMessage(
                                        sender_id =message.receiver_id,
                                        receiver_id =  "${message.sender_id}",
                                        content = message.content,
                                        time = System.currentTimeMillis().toString()
                                    ),false
                                )
                            }
                            catch(e:Exception){
                                Log.e("spec1","Failed to decode message:${e.message}")
                                _userState.value = UserState.Error(e.message.toString())
                            }
                }
            .launchIn(viewModelScope)
            Log.d("spec1", "Joining channel")
            messageChannel.join(blockUntilJoined = true)
                Log.d("spec1", "message joined successfully")
                isRealtimeInitialized = true
            }
        catch(e: Exception) {
            Log.e("spec1", e.message.toString())
        }
    }}
    suspend fun addingmessagetoRoom(message: ChatUserMessage,isme:Boolean){
        val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
        val uid = session.user?.id ?: throw Exception("No user ID found")
        messageRepository.addAmessage(
            messageDao = messageDao(
                textMessage = message.content,
                username = "${message.receiver_id}and${uid}",
                time = System.currentTimeMillis(),
                isme = isme,
                isseen = false,
                id = 0,
                isforwarded = "",
            )
        )
    }

    fun checkconnection(receiver_id: String){
        viewModelScope.launch {
            try {
                val session =
                    client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
                val uid = session.user?.id ?: throw Exception("No user ID found")
                val response = client.postgrest.from("request table")
                    .select(
                        columns = Columns.list("id,created_at,sender_id,reciever_id,status"),
                        head = false,
                        filter = {
                            and {
                                eq("sender_id" , "$uid")
                                eq("reciever_id" , "$receiver_id")
                            }
                        }
                    ).decodeSingle<conrequest>()
                Log.e("justcheck" , response.status)
                if(response.status == "Accepted"){
                    messageRepository.updatemessageTextMessage("connection${receiver_id}and${uid}" , "Accepted")
                }
                else{
                    if(response.status == "Rejected"){
                        messageRepository.updatemessageTextMessage("connection${receiver_id}and${uid}" , "Rejected")
                    }
                    else{

                    }
                }
            }
            catch (e:Exception){
                _userState.value = UserState.Error(e.message.toString())
            }
        }
    }
    fun connectionrequest(
        receiver_id: String
    ){
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
                val uid = session.user?.id ?: throw Exception("No user ID found")
                val request = conrequest(
                    sender_id = uid,
                    reciever_id = receiver_id,
                    status = "Pending"
                )
                client.postgrest["request table"].insert(request)
                messageRepository.addAmessage(
                    messageDao = messageDao(
                        textMessage = "Pending",
                        username = "connection${receiver_id}and${uid}",
                        time = 0L,
                        isme = false,
                        isseen = false,
                        isforwarded = "pending"
                    )
                )
            }
            catch (e : Exception){
               // Log.e("justcheck" , e.message.toString())
                _userState.value = UserState.Error(e.message.toString())
            }
        }
    }
     fun rejectConnection(sender_id: String){
         _userState.value = UserState.Loading
         viewModelScope.launch {

             try {
                 val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
                 val uid = session.user?.id ?: throw Exception("No user ID found")
                 client.postgrest["request table"].update(
                     update = {
                         set("status", "Rejected")
                     },
                     filter = {
                         and {
                             eq("sender_id", sender_id)
                             eq("reciever_id", uid)
                         }
                     }
                 )
             }
             catch (e:Exception){
                 _userState.value = UserState.Error(e.message.toString())
             }
         }
    }
    fun acceptConnection(sender_id: String){
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
                val uid = session.user?.id ?: throw Exception("No user ID found")
                client.postgrest["request table"].update(
                    update = {
                        set("status", "Accepted")
                    },
                    filter = {
                        and {
                            eq("sender_id", sender_id)
                            eq("reciever_id", uid)
                        }
                    }
                )
            }
            catch (e:Exception){
                _userState.value = UserState.Error(e.message.toString())
            }
        }
    }
    fun sendMessage(
        receiver_id: String,
        content: String,
        isforwarded:String,
    ) {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
                val uid = session.user?.id ?: throw Exception("No user ID found")
                    val message =if(isforwarded !="") { ChatUserMessage(
                        sender_id = uid,
                        receiver_id = receiver_id,
                        content = content,
                        time = System.currentTimeMillis().toString()
                    )
                }else{
                    ChatUserMessage(
                        sender_id = uid,
                        receiver_id = receiver_id,
                        content = content,
                        time = System.currentTimeMillis().toString(),
                    )
                }
                addmessage(
                    message = Message(
                        textMessage = message.content,
                        username = "You",
                        time = System.currentTimeMillis(),
                        isme = true,
                        id = 0,
                        false
                    )
                )
                client.postgrest["messages"].insert(message)
                addingmessagetoRoom(message,true)
                Log.d("spec1","message added to room ")
                _userState.value = UserState.loggedin
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Unknown error")
            }
        }
    }
     fun loadAllMessages(sender:String,uid:String , senderName:String) {
        viewModelScope.launch {
            try {
            val response = client.postgrest.from("messages")
                .select(
                    columns = Columns.list("id,created_at,sender_id,receiver_id,content,time"),
                    head = false,
                    filter = {
                        and {
                            eq("sender_id", sender)
                            eq("receiver_id", uid)
                    }
                    }
                )
                .decodeList<ChatUserMessage>()
                val convertedmessage =  response.mapNotNull {
                    chatUserMessage ->
                    chatUserMessage.tomessagedao(sender = senderName)
                }
                messageRepository.addlistofmessage(convertedmessage)
                client.postgrest.from("messages").delete(filter =
                {
                    and {
                        eq("sender_id", sender)
                        eq("receiver_id", uid)
                    }
                }
                )
        } catch (e: Exception) {
            Log.e("spec1", "Failed to fetch messages: ${e.message}")
            _userState.value = UserState.Error("Error loading messages: ${e.message}")
        } }
    }
    fun diconnectwebsoket(){
        viewModelScope.launch {
            try {
                Log.d("spec1","websoket disconnected")
                isRealtimeInitialized = false
                client.realtime.disconnect()
            }
            catch (e:Exception){
                Log.e("spec1",e.message.toString())
                _userState.value = UserState.Error(e.message.toString())
            }
        }
    }

    fun clearmessage(){
        _messages.clear()
    }
    fun messageDao.toMessage(sender: String):Message{
        val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
        return (
                Message(
                    textMessage = this.textMessage,
                    username = if(this.isme == true)"you" else sender,
                    time = this.time,
                    isme = isme,
                    id = this.id,
                    isseen = this.isseen
                )
                )
    }
    suspend fun isseen(id:Long,receiverId: String, content: String, time:String){
         try {
            val session = client.gotrue.currentSessionOrNull()
            val userId = session?.user?.id
            val result = client.postgrest.from("messages")
                .select(
                    count = Count.EXACT,
                    columns = Columns.list("id,created_at,sender_id,receiver_id,content,time"),
                    head = false,
                    filter = {
                        and {
                            if (userId != null) {
                                eq("sender_id", userId)
                            }
                            eq("receiver_id", receiverId)
                            eq("content", content)
                        }
                    }
                )
            Log.d("isseened", "${receiverId} ${userId} ${content} ${time}")
            messageRepository.updateseenstatus(id,result.count()?.toInt() == 0)
        } catch (e: Exception) {
            Log.e("isseened", "Error checking seen status", e)
            false
        }
    }
    fun ChatUserMessage.tomessagedao(sender:String):messageDao{
        val session = client.gotrue.currentSessionOrNull() ?: throw Exception("No active session")
        val uidi = session.user?.id ?: throw Exception("No user ID found")
        return (
                messageDao(
                    textMessage = this.content,
                    username = "${this.sender_id}and${uidi}",
                    time = this.time.toLong(),
                    isme = false,
                    isseen = false,
                    id = 0,
                    isforwarded = ""
                )
                )
    }

     suspend fun countemessages(receiverId:String) {
        try {
                val session = client.gotrue.currentSessionOrNull()
                val userId = session?.user?.id
                val result = client.postgrest.from("messages")
                    .select(
                        count = Count.EXACT,
                        columns = Columns.list("id,created_at,sender_id,receiver_id,content,time"),
                        head = false,
                        filter = {
                            and {
                                eq("sender_id", receiverId)
                                if (userId != null) {
                                    eq("receiver_id", userId)
                                }
                            }
                        }
                    )
                Log.e("message_count", "${receiverId} ${userId}")
            count.value = (result.count()?:"0").toString()
            Log.e("message_count",count.value )
        } catch (e: Exception) {
            _userState.value = UserState.Error(e.message.toString())
        }
    }

}