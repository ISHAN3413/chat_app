package com.example.globalchat.view.homelayouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.globalchat.R

@Composable
fun drawerContent(
    modifier: Modifier = Modifier,
    imageurl: String,
    about: String,
    name: String,
    updateprofile:()->Unit,
    logout:()->Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Your Profile",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding(
                start = (screenWidth * 0.1).dp,
                top = (screenHeight * 0.05).dp,
                bottom = (screenWidth * 0.05).dp
            )
        )
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(
                start = (screenWidth * 0.1).dp,
                end = (screenWidth * 0.1).dp,
                top = (screenHeight*0.05f).dp,
                bottom = (screenHeight*0.05f).dp
            ).fillMaxHeight(0.7f)
        ) {
            if (imageurl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageurl),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default Profile Icon",
                    modifier = Modifier.size(100.dp)
                )
            }
            Text(
                text = name.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = about,
                fontWeight = FontWeight.Medium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = (screenWidth * 0.05).dp
                )
            )
        }
        Divider(
            thickness = 1.5.dp,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(
                start = (screenWidth * 0.1).dp,
                end = (screenWidth * 0.1).dp,
                top = (screenHeight * 0.05).dp,
                bottom= (screenHeight* 0.05).dp
            )
        ) {
            Row(modifier = Modifier.clickable( interactionSource = remember{MutableInteractionSource()},
                indication = null) {updateprofile() }.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.baseline_edit_24), // Use appropriate icon
                    contentDescription = "Update Profile",
                    tint = Color.Black,
                    modifier = Modifier.padding(end=15.dp)
                )
                Text(text = "Update Profile", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            Row(modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { logout() }.padding(top = (screenHeight*0.02).dp).fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.baseline_error_outline_24), // Use appropriate icon
                    contentDescription = "Log Out",
                    tint = Color.Black,
                    modifier = Modifier.padding(end=15.dp)
                )
                Text(text = "Log out", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(modifier = Modifier.fillMaxSize(0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGreeting() {
    drawerContent(
        imageurl = "",
        about = "I am an Indian",
        name = "Ishan",
        updateprofile = {},
        logout = {}
    )
}