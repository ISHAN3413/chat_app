package com.example.globalchat.view.HomeLayouts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.MyAppNavigation
import com.example.globalchat.R

@Composable
fun topappbar(modifier: Modifier,Onclick1:()->Unit,Onclick2:()->Unit,imageurl:String){
    val primarycolor = MaterialTheme.colorScheme.primary
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val textcolor = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .offset(y=(-screenHeight*0.22f).dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

        ){
            if (imageurl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageurl),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .padding((screenWidth*0.1).dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default Profile Icon",
                    modifier = Modifier.size(120.dp).padding((screenWidth*0.1).dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Giga Chat",
                fontSize = (screenHeight*0.06).sp,
                color = textcolor,
                fontWeight = FontWeight.SemiBold,

            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(Onclick1,
                ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    contentDescription= "logout",
                modifier = Modifier,
                tint= textcolor
                )
            }
        }

}}