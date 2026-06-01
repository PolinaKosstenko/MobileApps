package com.example.harrypotterapi.ui.navigation

sealed class HarryPotterAPIRoute(val route: String) {
    data object CharacterCreation : HarryPotterAPIRoute("create")

    data object CharacterDetail : HarryPotterAPIRoute("character/{id}") {
        const val ARG_ID = "id"
        fun createRoute(id: Int): String = "character/$id"
    }

    data object CharacterList : HarryPotterAPIRoute("list")
    data object Arriving : HarryPotterAPIRoute("arriving")
    data object Gryffindor : HarryPotterAPIRoute("gryffindor")
    data object Slytherin : HarryPotterAPIRoute("slytherin")
    data object Ravenclaw : HarryPotterAPIRoute("ravenclaw")
    data object Hufflepuff : HarryPotterAPIRoute("hufflepuff")
    data object SortingHat : HarryPotterAPIRoute("sortinghat")
    data object Lessons : HarryPotterAPIRoute("lessons")

    data object Learning : HarryPotterAPIRoute("learning/{id}") {
        const val ARG_ID = "id"
        fun createRoute(id: Int): String = "learning/$id"
    }

    data object Settings : HarryPotterAPIRoute("settings")
    data object Fired : HarryPotterAPIRoute("fired")
}
