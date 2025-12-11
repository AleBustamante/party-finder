package com.teamadn.partyfinder.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Github: Screen("github")
    object Profile: Screen("profile")
    object CardExamples: Screen("card")
    object Dollar: Screen("dollar")
    object PopularMovies: Screen("popularMovies")
    object Party: Screen("party")

    object Login: Screen("login")
    object Register: Screen("register")

    object Favorites: Screen("favorites")
}