package com.example.truecallercopy.ui.view

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import com.example.truecallercopy.ui.viewmodel.CallerViewModel

@Composable
fun CallerNavGraph(viewModel: CallerViewModel) {
    val navController = rememberNavController()

//    NavHost(navController, startDestination = "popup") {
//        composable("popup") {
//            IncomingCallPopup(viewModel, onDismiss = { navController.navigate("dialer") })
//        }
//        composable("dialer") {
//            NewDialer()
//        }
//    }
}
