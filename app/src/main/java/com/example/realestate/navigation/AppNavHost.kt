package com.example.realestate.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.realestate.ui.details.DetailsScreen
import com.example.realestate.ui.details.DetailsViewModel
import com.example.realestate.ui.home.HomeScreen
import com.example.realestate.ui.home.HomeViewModel

object Routes {
    const val HOME = "home"
    const val DETAILS = "details/{propertyId}"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(
                state = vm.uiState,
                onToggleFavorite = vm::onToggleFavorite,
                onSearchQueryChange = vm::onSearchQueryChange,
                onPropertyClick = { id ->
                    navController.navigate("details/$id")
                }
            )
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("propertyId") { type = NavType.LongType })
        ) {
            val vm: DetailsViewModel = hiltViewModel()
            DetailsScreen(
                state = vm.uiState,
                onToggleFavorite = vm::onToggleFavorite,
                onBack = { navController.popBackStack() }
            )
        }
    }
}