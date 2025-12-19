package com.example.pathfindergo.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pathfindergo.navigation.Screen
import com.example.pathfindergo.ui.viewmodels.AuthViewModel
import com.example.pathfindergo.ui.viewmodels.RouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    routeViewModel: RouteViewModel
) {
    val user = authViewModel.currentUser
    val routeState by routeViewModel.state.collectAsState()

    // Load routes when the screen opens
    LaunchedEffect(Unit) {
        user?.uid?.let { routeViewModel.loadRoutes(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PathFinder GO") },
                actions = {
                    // Button to go to Profile
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    // Logout Button
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.CreateRoute.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Route")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Welcome Section
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = user?.email ?: "Trainer",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Section
            Text(text = "Your Statistics", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(
                    label = "Total Routes",
                    value = "${routeState.routes.size}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Total KM",
                    value = String.format("%.2f", routeState.routes.sumOf { it.totalDistanceKm }),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Navigation Buttons
            Button(
                onClick = { navController.navigate(Screen.RouteList.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.List, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("View My Route Library")
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}