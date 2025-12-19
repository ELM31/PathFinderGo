package com.example.pathfindergo.ui.screens.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pathfindergo.data.models.Route
import com.example.pathfindergo.navigation.Screen
import com.example.pathfindergo.ui.viewmodels.RouteViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListScreen(
    navController: NavController,
    routeViewModel: RouteViewModel
) {
    val uiState by routeViewModel.state.collectAsState()

    // Search and Sort State
    var searchQuery by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("Date") } // Options: Date, Name, Distance

    // Derived state: Filter and Sort the list whenever search, sort, or the routes change
    val filteredRoutes = remember(uiState.routes, searchQuery, sortBy) {
        uiState.routes
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedWith(when (sortBy) {
                "Name" -> compareBy { it.name.lowercase() }
                "Distance" -> compareByDescending { it.totalDistanceKm }
                else -> compareByDescending { it.timestamp }
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Route Library") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search routes...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            // Sorting Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Date", "Name", "Distance").forEach { option ->
                    FilterChip(
                        selected = sortBy == option,
                        onClick = { sortBy = option },
                        label = { Text(option) }
                    )
                }
            }

            // List of Routes
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (filteredRoutes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No routes found", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRoutes) { route ->
                        RouteListItem(
                            route = route,
                            onClick = { navController.navigate(Screen.RouteDetail.createRoute(route.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RouteListItem(route: Route, onClick: () -> Unit) {
    val date = remember(route.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(Date(route.timestamp))
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${route.totalPoints} points",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = date, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = String.format("%.2f km", route.totalDistanceKm),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}