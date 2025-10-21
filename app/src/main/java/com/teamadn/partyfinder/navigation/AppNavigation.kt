package com.teamadn.partyfinder.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teamadn.partyfinder.features.auth.presentation.LoginScreen
import com.teamadn.partyfinder.features.auth.presentation.RegisterScreen
import com.teamadn.partyfinder.features.party.presentation.PartyScreen

@Composable
fun AppNavigation(navigationViewModel: NavigationViewModel) {
    val navController: NavHostController = rememberNavController()

    // Manejar navegación desde el ViewModel
    LaunchedEffect(Unit) {
        navigationViewModel.navigationCommand.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        // Configuración del back stack según sea necesario
                        when (command.options) {
                            NavigationOptions.CLEAR_BACK_STACK -> {
                                popUpTo(0) // Limpiar todo el back stack
                            }
                            NavigationOptions.REPLACE_HOME -> {
                                popUpTo(Screen.Party.route) { inclusive = true }
                            }
                            else -> {
                                // Navegación normal
                            }
                        }
                    }
                }
                is NavigationViewModel.NavigationCommand.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        //startDestination = Screen.Party.route // Pantalla de inicio
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Party.route) {
            PartyScreen()
        }

        // NUEVO: Rutas de autenticación
        composable(Screen.Login.route) {
            LoginScreen()
        }
        composable(Screen.Register.route) {
            RegisterScreen()
        }
    }
}