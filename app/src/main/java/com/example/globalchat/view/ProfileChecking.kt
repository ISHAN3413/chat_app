package com.example.globalchat.view
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.globalchat.model.Note
import kotlinx.coroutines.delay
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileChecking(name:String, aboutmain: String,uid:String, viewModel: AuthViewModel , navController: NavController) {
    val visibleText = remember { mutableStateOf("") }
    val typingDone = remember { mutableStateOf(false) }
    val showCursor = remember { mutableStateOf(false) }
    val restart = remember { mutableStateOf(true) }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    val bgcolor = MaterialTheme.colorScheme.tertiary
    var i = 0
    val about = "Hey!\nmake connection with \n" + name

        LaunchedEffect (uid) {
            viewModel.readFile("photos", "newImage", "${uid}") {
                imageUrl = it
            }
        }
    LaunchedEffect(restart.value) {
        if(restart.value) {
            visibleText.value = ""
            typingDone.value = false
            for (char in about) {
                visibleText.value += char
                delay(100L)
            }
            typingDone.value = true
            restart.value = false
        }
    }
    LaunchedEffect(typingDone.value) {
        if (typingDone.value) {
            while (true && i!=10) {
                showCursor.value = !showCursor.value
                delay(500L)
                i++
            }
            i=0
            visibleText.value = ""
            restart.value = true
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.tertiary)){
        Image(
            painter =  if(isSystemInDarkTheme())rememberAsyncImagePainter(R.drawable.chatback) else rememberAsyncImagePainter(R.drawable.chatback4),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
            contentScale = ContentScale.Crop
        )
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Transparent)
        .padding(top = 200.dp)
        .height(500.dp)) {
        val width = size.width
        val height = size.height
        val arcHeight = height*0.5f
        val path = Path().apply {
            arcTo(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = Size(width, arcHeight)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(path = path, color = bgcolor)
    }}
    val startAnimation = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        startAnimation.value = true
    }
    AnimatedVisibility(
        visible = startAnimation.value,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(
                    animationSpec = tween(800),
                    initialOffsetY = { fullHeight -> fullHeight / 2 }
                ) + scaleIn(initialScale = 0.8f, animationSpec = tween(800)),
        exit = fadeOut()
    ) {
        val scrollablestate = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollablestate)
                .padding(12.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top
        ) {

                IconButton(
                    onClick = {
                       navController.navigateUp()
                    },
                    modifier = Modifier.size(50.dp).padding(top = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrowleft),
                        contentDescription = "GO to home page",
                        tint = if(isSystemInDarkTheme()) Color.White else Color.Black,
                    )
                }

            Text(
                color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier.padding(5.dp).fillMaxWidth().height(230.dp),
                fontSize = 40.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start,
                text = visibleText.value + if (typingDone.value && showCursor.value) "l" else "",
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(160.dp).background(MaterialTheme.colorScheme.tertiary)
                )
                {
                    val rotation = remember { Animatable(0f) }
                    LaunchedEffect(startAnimation.value) {
                        if (startAnimation.value) {
                            rotation.animateTo(
                                targetValue = 360f,
                                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                            )
                        }
                    }
                    Image(
                        painter = if (imageUrl != "") rememberAsyncImagePainter(imageUrl) else rememberAsyncImagePainter(
                            R.drawable.ai
                        ),
                        contentDescription = "Profile Image",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .graphicsLayer(rotationZ = rotation.value),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                modifier = Modifier.padding(top = 25.dp),
                color = MaterialTheme.colorScheme.onBackground,
                text = aboutmain,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            var keycheck by remember { mutableStateOf("") }
          viewModel.loadlastmessage("connection${uid}" , onlastmessage = {
                keycheck = it.textMessage
              //Log.e("justcheck" ,keycheck )
            })
                viewModel.checkconnection(uid)
            Button(
                onClick ={ if(keycheck == "Pending"){ }
                else viewModel.connectionrequest(uid) },
                modifier = Modifier.fillMaxWidth().padding(top = 50.dp , bottom = 100.dp),
                enabled = keycheck != "Pending" || keycheck != "Accepted" || keycheck != "Rejected"
            ) {
                if(keycheck == "Pending" || keycheck == "Accepted" || keycheck == "Rejected"){
                    Text(keycheck, color = MaterialTheme.colorScheme.onBackground)
                }
                else Text("Connect", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
    }
@Preview(showBackground = true)
@Composable
 fun GreetingPreview() {
    ProfileChecking(
        viewModel = TODO(),
        name = "Ishan Agarwal",
        aboutmain = "Kotlin android developer | DSA entusiast",
        uid = TODO(),
        navController = TODO()
    )
}