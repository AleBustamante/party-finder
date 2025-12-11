package com.teamadn.partyfinder.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teamadn.partyfinder.features.auth.presentation.LoginScreen
import com.teamadn.partyfinder.features.auth.presentation.RegisterScreen
import com.teamadn.partyfinder.features.favorites.presentation.FavoriteScreen
import com.teamadn.partyfinder.features.party.presentation.PartyScreen

@Composable
fun AppNavigation(navigationViewModel: NavigationViewModel) {
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(key1 = Unit) {
        navigationViewModel.navigationCommand.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    if (command.options == NavigationOptions.CLEAR_BACK_STACK) {
                        navController.navigate(command.route) {
                            popUpTo(0)
                        }
                    } else {
                        navController.navigate(command.route)
                    }
                }
                is NavigationViewModel.NavigationCommand.PopBackStack -> navController.popBackStack()
            }
        }
    }
    NavHost(
        navController = navController,
        //startDestination = Screen.Party.route // Pantalla de inicio
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Party.route) {
            PartyScreen(navigationViewModel = navigationViewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(navigationViewModel = navigationViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navigationViewModel = navigationViewModel)
        }

        composable(Screen.Favorites.route) {
            FavoriteScreen()
        }
    }
}