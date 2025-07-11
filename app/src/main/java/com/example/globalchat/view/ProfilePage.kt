package com.example.globalchat.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import coil.compose.rememberAsyncImagePainter

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.DAO.Graph
import com.example.globalchat.model.DAO.MessageRepository

import com.example.globalchat.model.UserState
import com.example.globalchat.uriToByteArray

@Composable
fun ProfilePage(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var firstname by rememberSaveable { mutableStateOf("") }
    var lastname by rememberSaveable{ mutableStateOf("") }
    var imageUri by rememberSaveable  { mutableStateOf<Uri?>(null) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val textcolor = MaterialTheme.colorScheme.onBackground
    val primarycolor = MaterialTheme.colorScheme.primary
    val authstate = authViewModel.userState.observeAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val ImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(authstate.value) {
        when (val state = authstate.value) {
            is UserState.profilefilled -> navController.navigate("home")
            is UserState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, height * 0.55f)
                quadraticBezierTo(width * 0.25f, height * 0.5f, width * 0.5f, height * 0.6f)
                quadraticBezierTo(width * 0.75f, height * 0.7f, width, height * 0.6f)
                lineTo(width, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primarycolor,
                style = Fill
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomEnd)
                .offset(y = -(screenHeight * 0.1f))
                .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Profile",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = textcolor,
                    fontSize = 35.sp
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUri),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .clip(CircleShape)
                                .fillMaxSize()
                                .clickable { ImagePickerLauncher.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        IconButton(
                            onClick = { ImagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .size(100.dp)
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Set profile picture",
                                modifier = Modifier.size(100.dp),
                                tint = textcolor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    textStyle = TextStyle(color = textcolor),
                    label = { Text("First Name", color = textcolor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    textStyle = TextStyle(color = textcolor),
                    label = { Text("About", color = textcolor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box {
                    when (authstate.value) {
                        is UserState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                        else -> {
                            Button(
                                onClick = {
                                    val imageByteArray = imageUri?.uriToByteArray(context)
                                    if (imageByteArray != null) {
                                        authViewModel.createBucket("photos")
                                        authViewModel.uploadFile("photos",  "newImage", imageByteArray)
                                        authViewModel.saveNote(name = firstname, about = lastname)
                                    }
                                    focusManager.clearFocus()
                                },
                                enabled = firstname.isNotBlank() && lastname.isNotBlank() && imageUri != null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Create Profile")
                            }
                        }
                    }
                }
            }
        }
    }
}

