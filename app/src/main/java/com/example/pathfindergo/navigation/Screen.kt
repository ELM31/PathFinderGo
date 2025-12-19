package com.example.pathfindergo.navigation

sealed class Screen(val route: String) {
    // Auth Screens
    object Login : Screen("login")
    object SignUp : Screen("signup")

    // Main App Screens
    object Dashboard : Screen("dashboard")
    object RouteList : Screen("route_list")
    object CreateRoute : Screen("create_route")

    // Detailed Screens (Notice the argument in the route for Detail/Edit)
    object RouteDetail : Screen("route_detail/{routeId}") {
        fun createRoute(routeId: String) = "route_detail/$routeId"
    }
    object EditRoute : Screen("edit_route/{routeId}") {
        fun createRoute(routeId: String) = "edit_route/$routeId"
    }

    object Profile : Screen("profile")
}