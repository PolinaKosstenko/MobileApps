package com.example.harrypotterapi.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.harrypotterapi.R
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.ui.screen.CharacterCreationScreen
import com.example.harrypotterapi.ui.screen.CharacterDetailScreen
import com.example.harrypotterapi.ui.screen.CharacterListScreen
import com.example.harrypotterapi.ui.screen.LearningScreen
import com.example.harrypotterapi.ui.screen.LessonsScreen
import com.example.harrypotterapi.ui.screen.QuestionScreen
import com.example.harrypotterapi.ui.screen.SettingsScreen
import com.example.harrypotterapi.ui.screen.VideoScreen
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UserViewModel
import com.example.harrypotterapi.ui.widget.Background
import com.example.harrypotterapi.viewmodel.QuizViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    holder: HarryPotterAPIViewModel = viewModel(),
    userHolder: UserViewModel = viewModel(),
    quizHolder: QuizViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var imageRes = 0;
    val userCharacter by userHolder.userCharacter.collectAsState()
    val userLocation by userHolder.userLocation.collectAsState()

    val isLoading by userHolder.isLoading.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    imageRes = if (
        userCharacter.house == null ||
        navController.currentDestination?.route == null ||
        navController.currentDestination?.route == HarryPotterAPIRoute.CharacterCreation.route
    ) {
        R.drawable.letter
    }
    else when (userLocation) {
        House.Gryffindor -> R.drawable.gryffindor
        House.Slytherin -> R.drawable.slytherin
        House.Ravenclaw -> R.drawable.ravenclaw
        House.Hufflepuff -> R.drawable.hufflepuff
        else -> R.drawable.greathall
    }

    Background(imageRes) {
        NavHost(
            navController = navController,
            startDestination =
                if (userCharacter.id == Int.MIN_VALUE) HarryPotterAPIRoute.CharacterCreation.route
                else HarryPotterAPIRoute.CharacterList.route,
            modifier = modifier
        ) {

            composable(
                route = HarryPotterAPIRoute.CharacterCreation.route
            ) {
                val userCharacter by userHolder.userCharacter.collectAsState()
                CharacterCreationScreen(userCharacter, userHolder, {
                    navController.navigate(
                        HarryPotterAPIRoute.Arriving.route
                    )
                })
            }

            composable(route = HarryPotterAPIRoute.CharacterList.route) {

                val filters by holder.filterStateFlow.collectAsState()

                CharacterListScreen(
                    holder,
                    filters = filters,
                    onSetLocation = userHolder::onSetLocation
                ) { id ->
                    navController.navigate(
                        HarryPotterAPIRoute.CharacterDetail.createRoute(id)
                    )
                }
            }

            composable(
                route = HarryPotterAPIRoute.Arriving.route,
            ) {
                VideoScreen(R.raw.hogwarts, {
                    navController.navigate(
                        HarryPotterAPIRoute.SortingHat.route
                    )
                })
            }

            composable(
                route = HarryPotterAPIRoute.SortingHat.route
            ) {
                QuestionScreen(quizHolder, { house ->
                    when (house) {
                        House.Gryffindor -> navController.navigate(
                            HarryPotterAPIRoute.Gryffindor.route
                        )
                        House.Slytherin -> navController.navigate(
                            HarryPotterAPIRoute.Slytherin.route
                        )
                        House.Ravenclaw -> navController.navigate(
                            HarryPotterAPIRoute.Ravenclaw.route
                        )
                        else -> navController.navigate(
                            HarryPotterAPIRoute.Hufflepuff.route
                        )
                    }
                })
            }

            composable(
                route = HarryPotterAPIRoute.Gryffindor.route,
            ) {
                VideoScreen(R.raw.gryffindor, {
                    userHolder.onHouseSorted(House.Gryffindor)
                    navController.navigate(
                        HarryPotterAPIRoute.CharacterList.route
                    )
                })
            }

            composable(
                route = HarryPotterAPIRoute.Slytherin.route,
            ) {
                VideoScreen(R.raw.slytherin, {
                    userHolder.onHouseSorted(House.Slytherin)
                    navController.navigate(
                        HarryPotterAPIRoute.CharacterList.route
                    )
                })
            }

            composable(
                route = HarryPotterAPIRoute.Ravenclaw.route,
            ) {
                VideoScreen(R.raw.ravenclaw, {
                    userHolder.onHouseSorted(House.Ravenclaw)
                    navController.navigate(
                        HarryPotterAPIRoute.CharacterList.route
                    )
                })
            }

            composable(
                route = HarryPotterAPIRoute.Hufflepuff.route,
            ) {
                VideoScreen(R.raw.hufflepuff, {
                    userHolder.onHouseSorted(House.Hufflepuff)
                    navController.navigate(
                        HarryPotterAPIRoute.CharacterList.route
                    )
                })
            }

            composable(
                route = HarryPotterAPIRoute.Fired.route,
            ) {
                VideoScreen(R.raw.fired, {
                    navController.navigate(
                        HarryPotterAPIRoute.CharacterCreation.route
                    )
                })
            }

            composable(
                route = HarryPotterAPIRoute.CharacterDetail.route,
                arguments = listOf(
                    navArgument(HarryPotterAPIRoute.CharacterDetail.ARG_ID) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                backStackEntry.arguments?.getInt(
                    HarryPotterAPIRoute.CharacterDetail.ARG_ID
                )?. let {
                    CharacterDetailScreen(
                        holder,
                        it,
                        onToggleFavourite = holder::onToggleFavourite,
                        onLoadList = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(
                route = HarryPotterAPIRoute.Lessons.route,
            ) {
                val userGrades = userHolder.getUserGrades().collectAsState(mapOf()).value

                LessonsScreen(holder, userGrades) {
                    id ->
                        navController.navigate(
                            HarryPotterAPIRoute.Learning.createRoute(id)
                        )
                }
            }

            composable(
                route = HarryPotterAPIRoute.Learning.route,
                arguments = listOf(
                    navArgument(HarryPotterAPIRoute.Learning.ARG_ID) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                backStackEntry.arguments?.getInt(
                    HarryPotterAPIRoute.CharacterDetail.ARG_ID
                )?.let {
                    LearningScreen(
                        holder,
                        it,
                    ) { spellName, deviation ->
                        userHolder.onDrawingComplete(spellName, deviation)
                        navController.navigate(
                            HarryPotterAPIRoute.Lessons.route
                        )
                    }
                }
            }

            composable(
                route = HarryPotterAPIRoute.Settings.route,
            ) {
                SettingsScreen(
                    userHolder::resetUserGrades,
                    {
                        holder.deleteCharacter(userCharacter.id)
                        userHolder.resetUserCharacter()
                        navController.navigate(
                            HarryPotterAPIRoute.Fired.route
                        )
                    },
                    userHolder::setUserSelectedTheme
                )
            }
        }
    }
}
