package com.example.globalchat.view.HomeLayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.globalchat.ViewModels.AuthViewModel

@Composable
fun alertdialog(dialogopen: MutableState<Boolean>, id:String, isall: MutableState<Boolean>, authViewModel: AuthViewModel){
    if(dialogopen.value) {
        AlertDialog(
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = {
                dialogopen.value = false
            },
           confirmButton = {
               TextButton( onClick = {
                   if(isall.value) {
                       authViewModel.deleteallmessages(id)
                   }else{
                       authViewModel.deletemessagebyid()
                   }
                   dialogopen.value = false
               }) {
                   if(isall.value) {
                       Text(
                           fontWeight = FontWeight.Bold,
                           color = colorScheme.primary,
                           text = "Clear chat"
                       )
                   }else{
                       Text(
                           fontWeight = FontWeight.Bold,
                           color = colorScheme.primary,
                           text ="Delete for me"
                       )
                   }
               }
           },
            dismissButton = {
                androidx.compose.material.TextButton(onClick = {
                   dialogopen.value = false
                }) {
                    Text(color = colorScheme.primary,fontWeight = FontWeight.Bold,text = "Dismiss")
                }
            },
            title = {
                if(isall.value) {
                    Text("Clear this Chat?")
                }else{
                    Text("Delete selected messages?")
                }
            },
            text = {
                if(isall.value) {
                    Text(
                        "Are you sure, You want to delete all the messages from this chat ?\n" +
                                "By confirming this it would permanently delete all the messages from this chat permanently form your device"
                    )
                }else{
                    Text(
                        "Are you sure, You want to delete all the selected messages from this chat ?\n" +
                                "By confirming this it would permanently delete all the messages that you have selected permanently form your device"
                    )
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
        )
    }
}