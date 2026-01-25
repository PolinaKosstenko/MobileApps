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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.screen.CharacterDetailScreen
import com.example.harrypotterapi.ui.screen.CharacterListScreen
import kotlin.collections.listOf


sealed class HarryPotterAPIRoute(val route: String) {

    data object CharacterDetail : HarryPotterAPIRoute("character/{id}") {
        const val ARG_ID = "id"
        fun createRoute(id: Int): String = "character/$id"
    }

    data object CharacterList : HarryPotterAPIRoute("list")
}

@Composable
fun App() {
    val holder: HarryPotterAPIViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    Scaffold() { padding -> 
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavHost(
                navController = navController,
                startDestination = HarryPotterAPIRoute.CharacterList.route
            ) {
                composable(route = HarryPotterAPIRoute.CharacterList.route) {
                    holder.loadCharacters()
                    CharacterListScreen(
                        holder.uiState,
                        holder.characters,
                        holder::setLoading,
                        holder::onSearchChange,
                        holder::onToggleWizardFilter,
                        holder::onToggleFavouritesFilter,
                        holder::onAllowHouse,
                    ) {
                        id -> navController.navigate(HarryPotterAPIRoute.CharacterDetail.createRoute(id))
                    }
                }

                composable(
                    route = HarryPotterAPIRoute.CharacterDetail.route,
                    arguments = listOf(navArgument(HarryPotterAPIRoute.CharacterDetail.ARG_ID) {
                        type = NavType.IntType
                    })) { backStackEntry ->
                        val id: Int? = backStackEntry.arguments?.getInt(
                            HarryPotterAPIRoute.CharacterDetail.ARG_ID)
                        if (id != null) {
                            CharacterDetailScreen(
                                holder.getCharacter(id),
                                holder::onToggleFavourite,
                                holder::isFavourite,
                                { navController.navigate(HarryPotterAPIRoute.CharacterList.route) }
                            )
                        }
                    }
            }

        }
    }
}
