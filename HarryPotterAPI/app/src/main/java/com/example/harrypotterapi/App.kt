package com.example.harrypotterapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding


import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.harrypotterapi.ui.navigation.AppNavigationBar
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UserViewModel
import com.example.harrypotterapi.viewmodel.QuizViewModel

@Composable
fun App() {
    val holder: HarryPotterAPIViewModel = viewModel()
    val userHolder: UserViewModel = viewModel()
    val quizHolder: QuizViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppNavigationBar(navController, holder, userHolder, quizHolder)
        }
    }
}
