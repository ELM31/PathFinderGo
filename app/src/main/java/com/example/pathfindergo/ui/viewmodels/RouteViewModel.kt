package com.example.pathfindergo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfindergo.data.models.Route
import com.example.pathfindergo.data.models.Waypoint
import com.example.pathfindergo.data.repository.RouteRepository
import com.example.pathfindergo.data.util.CoordinateParser
import com.example.pathfindergo.data.util.LocationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RouteViewModel(
    private val repository: RouteRepository = RouteRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> = _state

    // --- ADDED FOR DETAIL SCREEN ---
    // This holds the points for the specific route you are looking at
    private val _currentWaypoints = MutableStateFlow<List<Waypoint>>(emptyList())
    val currentWaypoints: StateFlow<List<Waypoint>> = _currentWaypoints

    // Fetch all routes for the dashboard/list
    fun loadRoutes(userId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val routes = repository.getRoutes(userId)
                _state.value = _state.value.copy(isLoading = false, routes = routes)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    // --- ADDED FOR DETAIL SCREEN ---
    // Fetch individual waypoints when the user clicks a specific route
    fun loadRouteDetails(routeId: String) {
        viewModelScope.launch {
            _currentWaypoints.value = repository.getWaypoints(routeId)
        }
    }

    // --- ADDED FOR DELETION ---
    fun deleteRoute(routeId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val result = repository.deleteRoute(routeId)
            result.onSuccess {
                // Refresh the list so the deleted route disappears
                _state.value.routes.firstOrNull()?.userId?.let { loadRoutes(it) }
                onComplete()
            }
        }
    }

    // Your "Magic" function for parsing and saving
    fun createRouteFromText(name: String, rawText: String, userId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // 1. Parse the text
            val rawWaypoints = CoordinateParser.parseTextToWaypoints(rawText)

            if (rawWaypoints.isEmpty()) {
                _state.value = _state.value.copy(isLoading = false, error = "No coordinates found")
                return@launch
            }

            // --- NEW: OPTIMIZE THE PATH ---
            val optimizedWaypoints = LocationUtils.optimizePath(rawWaypoints)
            // ------------------------------

            // 2. Calculate distance using the OPTIMIZED list
            val distance = LocationUtils.calculateTotalDistance(optimizedWaypoints)

            // 3. Create the Route object
            val newRoute = Route(
                userId = userId,
                name = name,
                totalPoints = optimizedWaypoints.size,
                totalDistanceKm = distance
            )

            // 4. Save the OPTIMIZED list to Firebase
            val result = repository.saveRoute(newRoute, optimizedWaypoints)

            result.onSuccess {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                loadRoutes(userId)
            }.onFailure { e ->
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(isSuccess = false)
    }
}