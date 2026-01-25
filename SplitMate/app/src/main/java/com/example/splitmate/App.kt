package com.example.splitmate

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.splitmate.ui.screen.HistoryScreen
import com.example.splitmate.ui.screen.InputScreen
import com.example.splitmate.ui.screen.HomeScreen
import com.example.splitmate.ui.screen.ResultScreen
import com.example.splitmate.ui.viewmodel.SplitMateUIState
import com.example.splitmate.ui.viewmodel.SplitMateViewModel
import kotlin.collections.listOf


sealed class SplitMateRoute(val route: String) {

    data object Home : SplitMateRoute("home")
    data object Input : SplitMateRoute("input/{changeLast}") {
        const val ARG_CHANGELAST = "changeLast"
        fun createRoute(changeLast: Boolean): String = "input/$changeLast"
    }

    data object Result : SplitMateRoute("result/{id}") {
        const val ARG_ID = "id"
        fun createRoute(id: Int): String = "result/$id"
    }

    data object History : SplitMateRoute("history")
}

@Composable
fun App() {
    val holder: SplitMateViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    Scaffold() { padding -> 
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavHost(navController = navController, startDestination = SplitMateRoute.Home.route) {
                composable(route = SplitMateRoute.Home.route) {
                    HomeScreen() {
                        navController.navigate(SplitMateRoute.Input.createRoute(false))
                    }
                }

                composable(
                    route = SplitMateRoute.Input.route,
                    arguments = listOf(navArgument(SplitMateRoute.Input.ARG_CHANGELAST) {
                        type = NavType.BoolType
                    })
                ) { backStackEntry ->
                    val changeLast: Boolean? = backStackEntry.arguments?.getBoolean(SplitMateRoute.Input.ARG_CHANGELAST)
                    if (changeLast != null) {
                        InputScreen(
                            holder.uiState,
                            holder.validTotal,
                            holder.validPeople,
                            holder.validTip,
                            holder::onChangeTotal,
                            holder::onChangePeople,
                            holder::onChangeTip,
                            { holder.onSaveCheck(changeLast) }
                        ) {
                            navController.navigate(SplitMateRoute.Result.createRoute(
                                holder.checks.size - 1))
                        }
                    }
                }

                composable(
                    route = SplitMateRoute.Result.route,
                    arguments = listOf(navArgument(SplitMateRoute.Result.ARG_ID) {
                        type = NavType.IntType
                    })) { backStackEntry ->
                        val id: Int? = backStackEntry.arguments?.getInt(SplitMateRoute.Result.ARG_ID)
                        if (id != null) {
                            ResultScreen(
                                holder.checks[id],
                                holder::tipAmount,
                                holder::totalWithTip,
                                holder::perPerson,
                                navController.previousBackStackEntry?.destination?.route !=
                                        SplitMateRoute.History.route,
                                { navController.popBackStack() },
                                {
                                    holder.resetState()
                                    navController.navigate(SplitMateRoute.Input.createRoute(false))
                                },
                                { navController.navigate(SplitMateRoute.History.route) }
                            )
                        }
                    }

                composable(route = SplitMateRoute.History.route) {
                    HistoryScreen(
                        holder.checks
                    ) { id -> navController.navigate(SplitMateRoute.Result.createRoute(id)) }
                }
            }

        }
    }
}
