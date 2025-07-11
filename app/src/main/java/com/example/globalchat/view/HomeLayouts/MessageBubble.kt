package com.example.globalchat.view.HomeLayouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.globalchat.R
import com.example.globalchat.ViewModels.AuthViewModel
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
 fun MessageBubble(message: Message, authViewModel: AuthViewModel, Receiverid:String){
    var isseen = remember { mutableStateOf(false) }
    if(message.isme && !message.isseen) {
        LaunchedEffect(Receiverid, message) {
           authViewModel.isseen(message.id,Receiverid, message.textMessage,message.time.toString())
        }
    }
    isseen.value = message.isseen
    val initialcolor = colorScheme.background
    val aftercolor = colorScheme.primary.copy(0.3f)
    var isenable = remember { mutableStateOf(false) }
    var rowcolor = remember { mutableStateOf(initialcolor) }
    if(!isenable.value) rowcolor.value  = initialcolor else rowcolor.value = aftercolor
    val selectedids = authViewModel.selectedids
    var textwidth = remember { mutableStateOf(2) }
    val density = LocalDensity.current
    val textWidthDp = with(density) { textwidth.value.toDp() }
    Row(
        verticalAlignment = Alignment.Bottom
    ){
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .background(rowcolor.value)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (isenable.value) {
                                selectedids.remove(message.id)
                                isenable.value = false
                            }
                        },
                        onLongPress = {
                            if (!isenable.value) {
                                selectedids.add(message.id)
                                isenable.value = true
                            }
                        }
                    )
                },
        ){
            Box(
                modifier = Modifier
                    .align(if (message.isme) Alignment.BottomEnd else Alignment.BottomStart)
                    .padding(
                        start = if (message.isme) 100.dp else 8.dp,
                        end = if (message.isme) 8.dp else 100.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (message.isme) colorScheme.primary else colorScheme.tertiary)
                    .padding(10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        modifier = Modifier.onGloballyPositioned {
                                coordinates ->
                            textwidth.value = coordinates.size.width
                        },
                        text = message.textMessage,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.width(textWidthDp - 25.dp))
                        Text(
                            text = formatTimestamp(message.time)[2],
                            color = Color.Black,
                            fontSize = 10.sp,
                            textAlign = TextAlign.End
                        )
                        if(message.isme) {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(18.dp)
                                    .align(Alignment.Top),
                                painter = painterResource(R.drawable.read),
                                contentDescription = "isseet",
                                tint = if (!isseen.value) Color.Gray else Color(105, 242, 243)
                            )
                        }
                    }
                }
            }
        }
    }
}


fun formatTimestamp(timestamp: Long): List<String> {
    val date = Date(timestamp)
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())

    val day = dayFormat.format(date)
    val formattedDate = dateFormat.format(date)
    val time = timeFormat.format(date)
    return listOf(day , formattedDate , time)
}
