package com.example.globalchat.view

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.globalchat.ViewModels.AuthViewModel
import com.example.globalchat.model.UserState

@Composable
fun SignUpPage(
   modifier: Modifier = Modifier,navController: NavController, authViewModel: AuthViewModel
){
   val context = LocalContext.current
   val userState = authViewModel.userState.observeAsState()
   var useremail by rememberSaveable{ mutableStateOf("") }
   var userpassword by rememberSaveable{ mutableStateOf("") }
   var confirmpassword by rememberSaveable { mutableStateOf("") }
   val screenHeight = LocalConfiguration.current.screenHeightDp.dp
   val focusManager = LocalFocusManager.current
   val textcolor = MaterialTheme.colorScheme.onBackground
   val primarycolor = MaterialTheme.colorScheme.primary
   LaunchedEffect (Unit){
      authViewModel.checkauthentication()
   }

   LaunchedEffect(userState.value) {
      when (val state = userState.value) {
         is UserState.authenticated -> navController.navigate("profile")
         is UserState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
         else -> Unit
      }
   }
   Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())){
   Box(modifier = Modifier.imePadding().fillMaxSize()) {
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
            .padding(top  = 300.dp )
            .pointerInput(Unit) {
               detectTapGestures { focusManager.clearFocus() }
            },
         verticalArrangement = Arrangement.Top,
         horizontalAlignment = Alignment.CenterHorizontally
      )  {
            Text(
               modifier = Modifier.align(Alignment.Start),
               text = "Sign up",
               textAlign = TextAlign.Center,
               fontWeight = FontWeight.Bold,
               color = textcolor,
               fontSize = 35.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
               value = useremail,
               onValueChange = { useremail = it },
               textStyle = TextStyle(color = textcolor),
               label = { Text("Email",color = textcolor) },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
               modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
               value = userpassword,
               onValueChange = { userpassword = it },
               textStyle = TextStyle(color = textcolor),
               label = { Text("Password",   color = textcolor,) },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
               visualTransformation = PasswordVisualTransformation(),
               modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
               value = confirmpassword,
               onValueChange = { confirmpassword = it },
               textStyle = TextStyle(color = textcolor),
               label = { Text("Confirm Password",   color = textcolor,) },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
               visualTransformation = PasswordVisualTransformation(),
               modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
               onClick = { if(userpassword == confirmpassword)authViewModel.signUp(useremail, userpassword) else Toast.makeText(context,"Password doesn't match",LENGTH_SHORT).show() },
               enabled = useremail.isNotBlank() && userpassword.isNotBlank() && confirmpassword.isNotBlank(),
               modifier = Modifier.fillMaxWidth()
            ) {
               Text(text = "Create Account",   color = textcolor,)
            }
            Spacer(modifier = Modifier.height(25.dp))
            TextButton(onClick = { navController.navigate("login") }) {
               Text(text = "Already hav an Account? ", color = textcolor,)
               Text(text = "Login", color = primarycolor)
            }
            Spacer(modifier = Modifier.height(20.dp))

         }

   }}
}


