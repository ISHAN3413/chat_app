package com.example.globalchat.view.HomeLayouts

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var textMessage: String,
    var username: String,
    var time: Long ,
    var isme: Boolean,
    var id:Long,
    var isseen:Boolean
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Chatpage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    Sender:String?,
    Receiver:String?
) {
    val view = LocalView.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    var scaffoldstate = rememberScaffoldState()
    val backcolor = colorScheme.background
    val dropcolor = colorScheme.onPrimary
    var istyping by rememberSaveable { mutableStateOf(false) }
    var Dialogopen = remember { mutableStateOf(false) }
    var isall = remember { mutableStateOf(false) }
    var user_message by rememberSaveable{ mutableStateOf("") }
    var messages =authViewModel.messages
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    var isforwarded = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = backcolor
        )
    }
    if (Sender != null) {
        if (Receiver != null) {
            authViewModel.loadAllMessages(Receiver,Sender,"")
        }
    }
    if(Sender!="Gemini") {
        LaunchedEffect(Unit) {
            authViewModel.initRealtimeMessaging(this,Receiver?:"" , Sender?:"")
            if (Receiver != null) {
                authViewModel.loadingmessagefromRoom(Receiver,Sender?:"")
            }
        }
    }
    else{
        authViewModel.clearmessage()
    }
    LaunchedEffect(messages.size) {
        if(messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    BackHandler {
        authViewModel.diconnectwebsoket()
        authViewModel.clearmessage()
        navController.navigateUp()
        authViewModel.selectedids.clear()
        messages.clear()
    }
    Scaffold(
        modifier = Modifier.background(colorScheme.background),
        scaffoldState = scaffoldstate,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsTopHeight(WindowInsets.statusBars)
                                .background(colorScheme.surface)
                        )
                        Text(
                            text = Sender ?: "You",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorScheme.onBackground
                        )
                        Text(
                            text = if (Sender == "Gemini") "AI Assistant" else "Online",
                            fontSize = 12.sp,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            authViewModel.clearmessage()
                            authViewModel.diconnectwebsoket()
                            navController.navigateUp()
                            authViewModel.selectedids.clear()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        if(authViewModel.selectedids.isNotEmpty()) {
                                IconButton(onClick = {
                                        Dialogopen.value = true
                                        isall.value = false

                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "More options",
                                        tint = colorScheme.onSurface
                                    )
                                }
                            }
                        else{
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    tint = colorScheme.onSurface
                                )
                            }
                        }
                        DropdownMenu(
                            modifier = Modifier.background(colorScheme.onTertiary),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Clear chat", color = dropcolor ) },
                                onClick = {
                                    if (Receiver != null) {
                                           Dialogopen.value = true
                                           isall.value = true
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Report", color = dropcolor) },
                                onClick = { expanded = false }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.onTertiary,
                    scrolledContainerColor = colorScheme.surface,
                    navigationIconContentColor = colorScheme.onSurface,
                    titleContentColor = colorScheme.onSurface,
                    actionIconContentColor = colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .shadow(elevation = 20.dp)
                    .padding(top = 10.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .imePadding()
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Divider(
                    thickness = 1.dp,
                    color = dropcolor,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(1f)
                        .fillMaxWidth(),
                    reverseLayout = false,
                    state = listState
                ) {
                    items(messages) { message ->
                                if (Receiver != null) {
                                    MessageBubble(message,authViewModel,Receiver)
                                }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp)),
                        value = user_message,
                        textStyle = TextStyle(colorScheme.onPrimary),
                        onValueChange = { user_message = it },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.outline,
                        ),
                        placeholder = {
                            Text(
                                text = "Type a message...",
                                color = colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if(user_message.isNotEmpty()){
                                if(Sender == "Gemini"){
                                    authViewModel.addmessage(
                                            Message(
                                                textMessage = user_message,
                                                time = System.currentTimeMillis(),
                                                username = "Gemini",
                                                isme = true,
                                                id = 0,
                                                isseen = false
                                            )
                                    )

                                    istyping = true
                                     coroutineScope.launch{
                                        authViewModel.sendMessagetoGemini(
                                        question = user_message)
                                    {aireply->
                                        authViewModel.addmessage(
                                            if(isforwarded.value == "") {
                                                Message(
                                                    textMessage = aireply,
                                                    time = System.currentTimeMillis(),
                                                    username = "Gemini",
                                                    isme = false,
                                                    id = 0,
                                                    isseen = false
                                                )
                                            }else{
                                                Message(
                                                    textMessage = aireply,
                                                    time = System.currentTimeMillis(),
                                                    username = "Gemini",
                                                    isme = false,
                                                    id = 0,
                                                    isseen = false
                                                )
                                            }
                                        )
                                    } }

                                }else{
                                    Receiver?.let { receiverId ->
                                        try{
                                            authViewModel.sendMessage(
                                                receiver_id = receiverId,
                                                content = user_message,
                                                isforwarded = isforwarded.value,
                                            )
                                        }
                                        catch (e:Exception){
                                            Log.e("Chatpage", "Message send failed", e)
                                        }
                                    }
                                }
                            }
                                user_message = ""
                            isforwarded.value = ""
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_send_24),
                            contentDescription = "Send message",
                            tint = colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    )
    if (Receiver != null) {
        alertdialog(Dialogopen,Receiver,isall,authViewModel)
    }
}
