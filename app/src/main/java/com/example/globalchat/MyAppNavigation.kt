package com.example.globalchat

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.view.HomeLayouts.Chatpage
import com.example.globalchat.view.HomePage
import com.example.globalchat.view.LoginPage
import com.example.globalchat.view.ProfileChecking
import com.example.globalchat.view.ProfilePage
import com.example.globalchat.view.SignUpPage
import com.example.globalchat.view.UpdateProfile

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("profile") {
            ProfilePage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignUpPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("updateProfile") {
            UpdateProfile(modifier, navController, authViewModel)
        }
        composable("profileChecking/{name}/{about}/{uid}") {backStackEntry->
            val Name = backStackEntry.arguments?.getString("name")?:""
            val About = backStackEntry.arguments?.getString("about")?:""
            val Uid = backStackEntry.arguments?.getString("uid")?:""
            ProfileChecking(
                Name,
                aboutmain = About,
                uid = Uid,
                viewModel = authViewModel,
                navController
            )
        }
        composable("chatPage/{Sender}/{Receiver}") {backStackEntry->
            val sender = backStackEntry.arguments?.getString("Sender")?:""
            val receiver = backStackEntry.arguments?.getString("Receiver")?:""
            Chatpage(modifier, navController, authViewModel,sender,receiver)
        }

    }
}
