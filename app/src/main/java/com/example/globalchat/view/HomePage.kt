package com.example.globalchat.view

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.DAO.messageDao
import com.example.globalchat.model.UserData
import com.example.globalchat.model.UserState
import com.example.globalchat.view.HomeLayouts.Message
import com.example.globalchat.view.HomeLayouts.MoreBottomSheet
import com.example.globalchat.view.HomeLayouts.alertdialogforconnection
import com.example.globalchat.view.HomeLayouts.formatTimestamp
import com.example.globalchat.view.homelayouts.drawerContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter",
    "NewApi"
)
@Composable

fun HomePage(modifier: Modifier,navController: NavController,authViewModel: AuthViewModel){
    val context = LocalContext.current
    val userState = authViewModel.userState.observeAsState()
    val userdata = authViewModel.userdata.observeAsState()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scope: CoroutineScope = rememberCoroutineScope()
    var name by rememberSaveable { mutableStateOf("Guest") }
    var about by rememberSaveable{ mutableStateOf("No information available") }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val textcolor = MaterialTheme.colorScheme.onPrimary
    var showdialog by remember{ mutableStateOf(false) }
    var ischecked by remember{ mutableStateOf(false) }
    val data by authViewModel.conrequest.observeAsState(emptyList())
    var imageUrl by rememberSaveable {
        mutableStateOf("")
    }
    var newimageUrl by rememberSaveable {
        mutableStateOf("")
    }
    val primarycolor = MaterialTheme.colorScheme.primary
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded}
    )
    LaunchedEffect(key1 = data , key2 = ischecked) {
        if(!ischecked && data.isNotEmpty()){
            showdialog = true
            ischecked = true
        }
    }
    LaunchedEffect(userdata.value) {
        when (val user = userdata.value) {
            is UserData.profile -> {
                name = user.name
                about = user.about
                Log.d("HOMEPGE","$name $about")
            }
            else -> {
                name = "Guest"
                about = "No information available"
            }
        }
        Log.d("HOMEPAGE", "User data updated: Name = $name, About = $about")
    }
    LaunchedEffect(userState.value) {
        when (val state = userState.value) {
            is UserState.unauthenticated -> navController.navigate("login")
            is UserState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            else -> Unit
        }
    }
    LaunchedEffect (Unit) {
        authViewModel.readFile("photos", "newImage", "") {
            imageUrl = it
        }
        authViewModel.fetchrequests()
    }
    LaunchedEffect (Unit){
        authViewModel.fetchNotes()
    }

    ModalBottomSheetLayout(
        sheetContent = {
            MoreBottomSheet(modifier, { authViewModel.LogOut() })
        },
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetElevation = 20.dp,
        sheetBackgroundColor = MaterialTheme.colorScheme.tertiary,
        sheetContentColor = textcolor
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Image(
                painter =  if(isSystemInDarkTheme())rememberAsyncImagePainter(R.drawable.chatback) else rememberAsyncImagePainter(R.drawable.chatback4),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        Scaffold (
        topBar = {
                TopAppBar(
                    title = {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ){
                            Text(
                                modifier = Modifier.padding(top = 17.dp),
                                text = "Welcome back,",
                                color = textcolor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = name.uppercase().split(" ").first(),
                                color = textcolor,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                            },

                    actions = {
                        IconButton(
                            modifier = Modifier.padding(top = 25.dp),
                            onClick = {
                                scope.launch{
                                    if(modalSheetState.isVisible)modalSheetState.hide()
                                    else modalSheetState.show()
                                }
                            }
                        ){

                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.padding(top = 25.dp),
                            onClick = {
                        scope.launch {
                          scaffoldState.drawerState.open()
                        }
                    })  {
                        if (imageUrl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Default Profile Icon"
                            )
                        }
                    }}
                    , backgroundColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                )
        },
        backgroundColor = Color.Transparent,
        scaffoldState=scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        drawerContent = {
                    drawerContent(modifier
                        .fillMaxSize(),
                        imageurl = imageUrl,
                        about = "$about",
                        name = "$name",
                        updateprofile = { navController.navigate("updateProfile") },
                        logout = {authViewModel.LogOut()})
        }
        , drawerBackgroundColor = MaterialTheme.colorScheme.tertiary
        , drawerGesturesEnabled = true
    ){
        userlistcolumn(navController = navController, modifier = Modifier.fillMaxSize() )
            if(showdialog && data.isNotEmpty()){
                authViewModel.readFile("photos", "newImage", data[0].sender_id) {
                    newimageUrl = it
                }
                alertdialogforconnection(
                    onRejected = {
                        authViewModel.rejectConnection(data[0].sender_id)
                    },
                    onAccepted = {
                        authViewModel.acceptConnection(data[0].sender_id)
                    },
                    onDismiss = {
                        ischecked = true
                    },
                    id = data[0].sender_id ,
                    imageurl = newimageUrl,
                    viewModel = authViewModel
                )
            }
    }
    } }}

@Composable
fun userlistcolumn(modifier: Modifier,navController: NavController,viewModel: AuthViewModel = viewModel()){
    val users by viewModel.instruments.observeAsState(emptyList())
    var searchquery by remember{ mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.fetchInstruments()
    }
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.fillMaxWidth()
    )
    val filteredname = users.filter {
        it.name.contains(searchquery , ignoreCase = true)
    }
    LazyColumn (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        item{
//            Surface (
//                modifier = Modifier.fillMaxWidth().height(150.dp).padding(top = 10.dp , bottom = 10.dp).padding(5.dp),
//                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
//                shape = RoundedCornerShape(10.dp),
//                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onTertiary)
//            ){
                LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ){
                items(filteredname,key = {Note -> Note.id!! }){
                        Note->
                    var imageurl by remember { mutableStateOf("") }
                    LaunchedEffect (Note.uid) {
                        viewModel.readFile("photos", "newImage", "${Note.uid}") {
                            imageurl = it
                        }
                    }
                    Column (
                        modifier = Modifier
                            .width(110.dp)
                            .padding(5.dp)
                            .clickable(onClick = {
                                navController.navigate("profileChecking/${Note.name}/${Note.about}/${Note.uid}")
                            }),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        val outerSize = 90.dp
                        val ringWidth = 6.dp
                        val gapWidth = 8.dp
                        val imageSize = outerSize - (ringWidth + gapWidth) * 2
                        Box(
                            modifier = Modifier.size(outerSize),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize(),
                                shape = CircleShape,
                                color = Color.Transparent,
                                border = BorderStroke(ringWidth, MaterialTheme.colorScheme.primary)
                            ) {}
                            Surface(
                                modifier = Modifier
                                    .size(outerSize - ringWidth * 2),
                                shape = CircleShape,
                                color = Color.Transparent
                            ) {}
                            Image(
                                painter = rememberAsyncImagePainter(imageurl),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(imageSize)
                                    .clip(CircleShape)
                            )
                        }
                        Text(
                            color = MaterialTheme.colorScheme.onBackground,
                            text = Note.name,
                            maxLines = 1,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            //}

        }
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(bottom = 10.dp)
                    .clickable {
                        navController.navigate("chatPage/${"Gemini"}/${""}")
                    },
                shape = RoundedCornerShape(5.dp),
                shadowElevation = 20.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                border = BorderStroke(2.dp , if(isSystemInDarkTheme())Color(160,	160,	160) else Color(41,	79,	107)  )
            ){
                Row(
                    modifier=  Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center
                    ){
                            Image(
                                painter = rememberAsyncImagePainter(R.drawable.ai),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                    }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ){
                        Text(
                            text = "Gemini",
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Hi, I am Gemini AI \n let's start a conversation",
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        items(users,key = {Note -> Note.id!! }){
            Note ->
            var count by remember { mutableStateOf("0") }
            var lastmessage by remember { mutableStateOf(
               Message(
                   textMessage = "",
                   username = "",
                   time = 0L,
                   isme = false,
                   id = 0,
                   isseen = false
               )
            ) }
           LaunchedEffect(Note.uid) {
                viewModel.countemessages(Note.uid)
               count = viewModel.count.value
           }
            Log.d("message_count",count)
            var imageurl by remember { mutableStateOf("") }
            LaunchedEffect (Note.uid) {
                viewModel.readFile("photos", "newImage", "${Note.uid}") {
                    imageurl = it
                }
                viewModel.loadlastmessage(Note.uid){
                    lastmessage = it
                }

            }
            var isseen = remember { mutableStateOf(false) }
            if(lastmessage.isme && !lastmessage.isseen) {
                LaunchedEffect(Note.uid, lastmessage) {
                    viewModel.isseen(lastmessage.id,Note.uid, lastmessage.textMessage,lastmessage.time.toString())
                }
            }
            isseen.value = lastmessage.isseen
            Log.d("lastmessage", lastmessage.textMessage)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(bottom = 10.dp)
                    .clickable {
                        navController.navigate("chatPage/${Note.name}/${Note.uid}")
                    },
                shape = RoundedCornerShape(5.dp),
                shadowElevation = 20.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                border = BorderStroke(2.dp , if(isSystemInDarkTheme())Color(160,	160,	160) else Color(41,	79,	107))
            ){
                Row(
                    modifier=  Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement =Arrangement.Center
                    ) {
                        if (imageurl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(imageurl),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(55.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Default Profile Icon",
                                modifier = Modifier.size(55.dp)
                            )
                        }
                    }
                    Column (
                        modifier = Modifier.padding(10.dp)
                    ){
                        Row(
                           horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = Note.name,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                            if(count!="0") {
                                Box(
                                    contentAlignment = Alignment.TopEnd,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(255, 56, 0))
                                ) {
                                    Text(
                                        text = count,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = Color.White
                                    )
                                }

                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if(lastmessage.isme) {
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(20.dp)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(R.drawable.read),
                                    contentDescription = "isseen",
                                    tint = if (!isseen.value) Color.Gray else Color(105, 242, 243)
                                )
                            }
                            Text(
                               modifier = if(lastmessage.textMessage!="") Modifier.fillMaxWidth(0.7f) else Modifier.fillMaxWidth(),
                                text = if(lastmessage.textMessage!="")lastmessage.textMessage else "Start conversation with ${Note.name}",
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Medium,
                                color = if(lastmessage.textMessage=="")Color(105, 242, 243) else Color.Black,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if(lastmessage.textMessage!="") {
                                Text(
                                    text = if(count != "0" )formatTimestamp(lastmessage.time)[2] else {
                                        if(formatTimestamp(System.currentTimeMillis())[1] == formatTimestamp(lastmessage.time)[1]) "Today" else formatTimestamp(lastmessage.time)[1]
                                    },
                                    textAlign = TextAlign.End,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                )
                            }
                        }}}}}}}



    




