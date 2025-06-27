package com.example.globalchat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.DAO.GlobalChatApp
import com.example.globalchat.model.DAO.Graph
import com.example.globalchat.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Graph.provide(applicationContext)
        enableEdgeToEdge()
        setContent {
           val isDarkTheme = isSystemInDarkTheme() // Automatically detect system theme
            AppTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
               MyAppNavigation(modifier = Modifier.padding(innerPadding), authViewModel = AuthViewModel() )}
                }
            }
        }
    }


