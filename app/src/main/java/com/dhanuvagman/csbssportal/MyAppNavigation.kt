package com.dhanuvagman.csbssportal

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dhanuvagman.csbssportal.pages.*
import kotlinx.serialization.Serializable
import androidx.activity.viewModels
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import com.dhanuvagman.csbssportal.pages.LoginScreen
import com.dhanuvagman.csbssportal.pages.NewHomeScreen


@Composable
fun MyAppNavigation(authViewModel: AuthViewModel) {
    val navController= rememberNavController()
    val authState=authViewModel.authState.observeAsState()
    NavHost(
        navController=navController,
        startDestination =when(authState.value){
            is AuthState.Authenticated ->HomeScreen
            else ->LoginScreen
        }
    ){
        composable<HomeScreen> { NewHomeScreen(authViewModel) }
        composable<LoginScreen> { LoginScreen(navController,authViewModel)}
        composable<LoadDatal> { }
    }

}
@Serializable
object LoadDatal
@Serializable
object LoginScreen
@Serializable
object HomeScreen