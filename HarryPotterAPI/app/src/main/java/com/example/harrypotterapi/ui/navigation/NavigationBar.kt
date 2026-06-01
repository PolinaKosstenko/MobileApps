package com.example.harrypotterapi.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UserViewModel
import com.example.harrypotterapi.viewmodel.QuizViewModel
import com.example.quiz.ui.theme.MainTheme
import com.example.quiz.ui.theme.ThemeStyle

@Composable
fun AppNavigationBar(
    navController: NavHostController,
    holder: HarryPotterAPIViewModel = viewModel(),
    userHolder: UserViewModel = viewModel(),
    quizHolder: QuizViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = HarryPotterAPIRoute.CharacterCreation.route
    var selectedDestination by rememberSaveable { mutableStateOf(startDestination) }

    val userCharacter by userHolder.userCharacter.collectAsState()
    val selectedTheme by userHolder.getUserSelectedTheme().collectAsState(ThemeStyle.NEUTRAL)
    MainTheme(selectedTheme) {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                if (userCharacter.house != null && userCharacter.house != House.NoHouse) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        windowInsets = NavigationBarDefaults.windowInsets
                    ) {
                        val navBarItemColors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color.White,
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = Color.White,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White,
                            disabledIconColor = Color.White,
                            disabledTextColor = Color.White,
                        )

                        NavigationBarItem(
                            selected = selectedDestination == HarryPotterAPIRoute.CharacterList.route,
                            onClick = {
                                navController.navigate(route = HarryPotterAPIRoute.CharacterList.route)
                                selectedDestination = HarryPotterAPIRoute.CharacterList.route
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = "Локация"
                                )
                            },
                            colors = navBarItemColors,
                            label = { Text("Локация") }
                        )
                        NavigationBarItem(
                            selected = selectedDestination == HarryPotterAPIRoute.Lessons.route,
                            onClick = {
                                navController.navigate(route = HarryPotterAPIRoute.Lessons.route)
                                selectedDestination = HarryPotterAPIRoute.Lessons.route
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Create,
                                    contentDescription = "Занятия"
                                )
                            },
                            colors = navBarItemColors,
                            label = { Text("Занятия") }
                        )
                        NavigationBarItem(
                            selected = selectedDestination == HarryPotterAPIRoute.Settings.route,
                            onClick = {
                                navController.navigate(route = HarryPotterAPIRoute.Settings.route)
                                selectedDestination = HarryPotterAPIRoute.Settings.route
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Настройки"
                                )
                            },
                            colors = navBarItemColors,
                            label = { Text("Настройки") }
                        )
                    }
                }

            }
        ) { contentPadding ->
            AppNavHost(
                navController,
                holder,
                userHolder,
                quizHolder,
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
            )
        }
    }
}
