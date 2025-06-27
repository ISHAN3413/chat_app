package com.example.globalchat.view

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.UserData
import com.example.globalchat.model.UserState
import com.example.globalchat.view.HomeLayouts.MoreBottomSheet
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
    var imageUrl by rememberSaveable {
        mutableStateOf("")
    }
    val primarycolor = MaterialTheme.colorScheme.primary
    val modalSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded}
    )
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
    ) {Scaffold (
        topBar = {
                TopAppBar(
                    title = {
                        Text(
                        text = "Giga Chat",
                        color = textcolor,
                        fontSize = (screenHeight*0.05).sp,
                        fontWeight = FontWeight.ExtraBold
                        )},
                    actions = {
                        IconButton(
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
                    navigationIcon = { IconButton(onClick = {
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
                        .padding(top = 22.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.08f)
                )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        scaffoldState=scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
        userlistcolumn(navController = navController, modifier = Modifier.fillMaxSize())
    }
    } }

@Composable
fun userlistcolumn(modifier: Modifier,navController: NavController,viewModel: AuthViewModel = viewModel()){
    val users by viewModel.instruments.observeAsState(emptyList())
    LaunchedEffect(Unit){
        viewModel.fetchInstruments()
    }
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        item{
            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ){
                items(users,key = {Note -> Note.id!! }){
                        Note->
                    var imageurl by remember { mutableStateOf("") }
                    LaunchedEffect (Note.uid) {
                        viewModel.readFile("photos", "newImage", "${Note.uid}") {
                            imageurl = it
                        }
                    }
                    Column (
                        modifier = Modifier.width(130.dp).padding(5.dp).clickable(onClick = {
                            navController.navigate("profileChecking/${Note.name}/${Note.about}/${Note.uid}")
                        }),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = rememberAsyncImagePainter(imageurl),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .padding(bottom=10.dp)
                                .size(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            color = MaterialTheme.colorScheme.onBackground,
                            text = Note.name,
                            maxLines = 1,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .padding(bottom = 15.dp)
                    .clickable {
                        navController.navigate("chatPage/${"Gemini"}/${""}")
                    },
                shape = RoundedCornerShape(15.dp),
                shadowElevation = 20.dp,
                color = MaterialTheme.colorScheme.tertiary
            ){
                Row(
                    modifier=  Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                    ){
                            Image(
                                painter = rememberAsyncImagePainter(R.drawable.ai),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                    }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ){
                        Text(
                            text = "Gemini",
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Hi,\n I am Gemini AI \n let's start a conversation",
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        items(users,key = {Note -> Note.id!! }){
            Note ->
            var count by remember { mutableStateOf("0") }
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
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .padding(bottom = 15.dp)
                    .clickable {
                        navController.navigate("chatPage/${Note.name}/${Note.uid}")
                    },
                shape = RoundedCornerShape(15.dp),
                shadowElevation = 20.dp,
                color = MaterialTheme.colorScheme.tertiary
            ){
                Row(
                    modifier=  Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =Arrangement.Center
                    ) {
                        if (imageurl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(imageurl),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Default Profile Icon",
                                modifier = Modifier.size(70.dp)
                            )
                        }
                    }
                    Column (
                        modifier = Modifier.padding(15.dp)
                    ){
                        Row(
                           horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = Note.name,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
                            if(count!="0") {
                                Box(
                                    contentAlignment = Alignment.Center,
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
                       Text(
                           text = Note.about,
                           textAlign = TextAlign.Start,
                           fontWeight = FontWeight.Medium,
                           fontSize = 15.sp,
                           maxLines = 3,
                           overflow = TextOverflow.Ellipsis
                       )
                    }}}}}}



    




