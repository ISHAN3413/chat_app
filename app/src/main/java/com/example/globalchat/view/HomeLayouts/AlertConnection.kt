package com.example.globalchat.view.HomeLayouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.UserState

@Composable
fun Alertdialogforconnection(onAccepted:()->Unit,imageurl:String,onRejected:()->Unit,onDismiss:()->Unit ,id:String , viewModel: AuthViewModel){
    val userState = viewModel.userState.observeAsState()
    var name = "Ishan Agarwal"
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
            modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ){
            Text("Connection Request")
                Spacer(
                    modifier = Modifier.fillMaxWidth(0.4f)
                )
            IconButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    tint = colorScheme.primary,
                    imageVector = Icons.Default.Close,
                    contentDescription = "close"
                )
            }
        }
             },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (imageurl != "") rememberAsyncImagePainter(imageurl) else rememberAsyncImagePainter(
                        R.drawable.ai
                    ),
                    contentDescription = "Profile Image",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = name,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },

        confirmButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                when(userState.value){
                    is UserState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    is UserState.Error, is UserState.success->
                    {
                        onDismiss()
                    }
                    else->{
                        TextButton(onClick = onAccepted) {
                            Text(
                                text = "Accept",
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        TextButton(onClick = onRejected) {
                            Text(
                                "Reject",
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }

        },
        modifier = Modifier.fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp),
        titleContentColor = colorScheme.onPrimary,
        textContentColor = colorScheme.onPrimary,
        containerColor = colorScheme.tertiary,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
    )}
