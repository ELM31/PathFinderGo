package com.example.pathfindergo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import com.example.pathfindergo.ui.viewmodels.AuthViewModel
import com.example.pathfindergo.ui.viewmodels.RouteViewModel
import com.example.pathfindergo.ui.screens.auth.LoginScreen
import com.example.pathfindergo.ui.screens.auth.SignUpScreen
import com.example.pathfindergo.ui.screens.dashboard.HomeScreen
import com.example.pathfindergo.ui.screens.routes.CreateRouteScreen
import com.example.pathfindergo.ui.screens.routes.RouteListScreen
import com.example.pathfindergo.ui.screens.routes.RouteDetailScreen
import com.example.pathfindergo.ui.screens.dashboard.ProfileScreen
import com.example.pathfindergo.ui.screens.routes.EditRouteScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier 
) {
    val routeViewModel: RouteViewModel = viewModel()
    // Check if user is logged in to decide the starting screen
    val startDestination = if (authViewModel.currentUser != null) {
        Screen.Dashboard.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // --- Auth Group ---
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // --- Main App Group ---
        composable(Screen.Dashboard.route) {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                routeViewModel = routeViewModel
            )
        }

        composable(Screen.RouteList.route) {
            RouteListScreen(
                navController = navController,
                routeViewModel = routeViewModel
            )
        }

        composable(Screen.CreateRoute.route) {
            CreateRouteScreen(
                navController = navController,
                authViewModel = authViewModel,
                routeViewModel = routeViewModel
            )
        }

        // Screens that need a Route ID
        composable(Screen.RouteDetail.route) { backStackEntry ->
            // Use the Elvis operator (?:) to provide an empty string if it's null
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""

            RouteDetailScreen(
                navController = navController,
                routeViewModel = routeViewModel,
                routeId = routeId // Now it's a guaranteed String
            )
        }
        composable(Screen.EditRoute.route) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
            EditRouteScreen(
                navController = navController,
                routeViewModel = routeViewModel,
                routeId = routeId
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                routeViewModel = routeViewModel
            )
        }
    }
}