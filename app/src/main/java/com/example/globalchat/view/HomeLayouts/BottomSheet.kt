package com.example.globalchat.view.HomeLayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.globalchat.R

@Composable
fun MoreBottomSheet(modifier: Modifier,Onclick:()->Unit){
    Box(
        Modifier.fillMaxWidth().height(300.dp).background(
            MaterialTheme.colorScheme.primary
        )
    ){
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row (modifier = Modifier.padding(16.dp)){
                Button(Onclick,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_error_outline_24),
                        contentDescription= "logout",
                        modifier = Modifier.padding(10.dp),
                        tint= Color.Black
                    )
                    Text(text ="Log out", fontSize = 20.sp)
                }
            }
        }
    }
}
