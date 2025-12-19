package com.example.pathfindergo.ui.viewmodels

import com.example.pathfindergo.data.models.Route

data class RouteState(
    val isLoading: Boolean = false,
    val routes: List<Route> = emptyList(),
    val error: String? = null,
    val isSuccess: Boolean = false // Used to navigate after saving
)