package com.example.appfirebase.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appfirebase.AuthState
import com.example.appfirebase.AuthViewModel

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.observeAsState()
    val userEmail by authViewModel.userEmail.observeAsState("")

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login") { popUpTo("home") { inclusive = true } }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Rodape()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Você está na Home", fontSize = 28.sp, color = Color(0xFFD21656))
                    Text("Email atual: $userEmail", fontSize = 18.sp)

                    Button(
                        onClick = { navController.navigate("update") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Editar Senha")
                    }

                    Button(
                        onClick = { navController.navigate("delete") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Deletar Conta")
                    }

                    TextButton(
                        onClick = { authViewModel.signout() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Logout")
                    }

                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                }
            }
        }
    }
}

