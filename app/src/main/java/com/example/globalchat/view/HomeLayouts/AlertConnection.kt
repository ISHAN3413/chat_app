package com.example.globalchat.view.HomeLayouts

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.DAO.MessageRepository
import com.example.globalchat.model.DAO.MessagesDao
import com.google.android.gms.auth.api.Auth

@Composable
fun alertdialogforconnection(onAccepted:()->Unit,onRejected:()->Unit,onDismiss:()->Unit ,id:String){
    var name = id
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(top = 150.dp , bottom = 150.dp , start = 20.dp , end = 20.dp)
                .fillMaxWidth(0.9f)
                .background(Color.Transparent),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp).fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxWidth()){
                    Spacer(modifier = Modifier.fillMaxWidth(0.9f))
                    IconButton(
                        onClick = {
                            onDismiss
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "dismiss",
                            tint = colorScheme.onPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
//                        painter = if (imageUrl != "") rememberAsyncImagePainter(imageUrl) else rememberAsyncImagePainter(
//                            R.drawable.ai
                        painter = rememberAsyncImagePainter( R.drawable.ai),
                        contentDescription = "Profile Image",
                        alignment = Alignment.Center,
                        modifier = Modifier.padding(0.dp)
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier =Modifier.padding(20.dp),
                        maxLines = 2,
                        text = name.uppercase(),
                        textAlign = TextAlign.Start,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimary
                    )
                }
                Text(
                    text = "Ishan Agarwal wants to connect with you ",
                    textAlign = TextAlign.Start,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onPrimary
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ){
                    Button(
                        onClick = { onAccepted },
                        modifier = Modifier.padding(10.dp).fillMaxWidth()
                    ) {
                        Text("Accept", color = colorScheme.primary)
                    }
                    Button(
                        onClick = { onRejected },
                        modifier = Modifier.padding(10.dp).fillMaxWidth()
                    ) {
                        Text("Reject", color = colorScheme.primary)
                    }
                }


            }
}}}

@Preview(showBackground = true)
@Composable
fun CustomAlertDialogPreview() {
    // Provide fake handlers for preview
    alertdialogforconnection(
        onDismiss = {},
        onAccepted = {},
        onRejected = {},
        id = ""
    )
}