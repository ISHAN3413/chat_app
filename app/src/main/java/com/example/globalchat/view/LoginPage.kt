package com.example.globalchat.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.DAO.MessageRepository
import com.example.globalchat.model.DAO.MessagesDao
import com.example.globalchat.model.UserState
import com.example.globalchat.ui.theme.AppTheme

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    AppTheme(darkTheme = isSystemInDarkTheme()) {
    val context = LocalContext.current
    val userState by authViewModel.userState.observeAsState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
        val textcolor = MaterialTheme.colorScheme.onBackground
    val primarycolor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        authViewModel.checkauthentication()
    }
        LaunchedEffect(userState) {
        userState?.let { state ->
            when (state) {
                is UserState.authenticated -> {
                    navController.navigate("home"){
                        popUpTo("login"){inclusive = true}
                    }
                }
                is UserState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                else ->Unit
            }
        }
    }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())){
        Box(modifier = Modifier.fillMaxSize().imePadding()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, height * 0.55f)
                    quadraticBezierTo(width * 0.25f, height * 0.5f, width * 0.5f, height * 0.6f)
                    quadraticBezierTo(width * 0.75f, height * 0.7f, width, height * 0.6f)
                    lineTo(width, 0f)
                    close()
                }

                drawPath(
                    path = path,
                    color = primarycolor,
                    style = Fill
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 300.dp , start = 20.dp, end = 20.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    fontWeight = FontWeight.Bold,
                    color = textcolor,
                    fontSize = 35.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    textStyle = TextStyle(color = textcolor),
                    label = { Text("Email", color = textcolor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = userPassword,
                    onValueChange = { userPassword = it },
                    textStyle = TextStyle(color = textcolor),
                    label = { Text("Password", color = textcolor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = { authViewModel.ForgetPassword(email = userEmail) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forget password?", color = primarycolor)
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = { authViewModel.Login(userEmail, userPassword) },
                    enabled = userEmail.isNotBlank() && userPassword.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login", color = textcolor)
                }

                Spacer(modifier = Modifier.height(25.dp))

                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("Don't have an account?", color = textcolor)
                    Text(" Sign up", color = primarycolor)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }}
        }
    }
}

