package com.example.appfirebase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appfirebase.pages.CadastrarCliente
import com.example.appfirebase.pages.ConsultarCliente
import com.example.appfirebase.pages.DeletePage
import com.example.appfirebase.pages.HomePage
import com.example.appfirebase.pages.LoginPage
import com.example.appfirebase.pages.SignupPage
import com.example.appfirebase.pages.UpdatePage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginPage(modifier,navController,authViewModel)
        }
        composable("signup"){
            SignupPage(modifier,navController,authViewModel)
        }
        composable("home"){
            HomePage(navController = navController, authViewModel = authViewModel)


        }
        composable("delete"){
            DeletePage(navController = navController, authViewModel = authViewModel)

        }

        composable("update"){
            UpdatePage(navController = navController, authViewModel = authViewModel)
<<<<<<< HEAD

        }

        composable("cadastrar"){
            CadastrarCliente(navController = navController)

        }

        composable("consultar"){
            ConsultarCliente(navController = navController)
=======
>>>>>>> b100530e6dc5786d91662f6b485e3c9041a2a941

        }
    })
}