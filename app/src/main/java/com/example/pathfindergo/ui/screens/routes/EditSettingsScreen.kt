package com.example.pathfindergo.ui.screens.routes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pathfindergo.ui.viewmodels.RouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRouteScreen(
    navController: NavController,
    routeViewModel: RouteViewModel,
    routeId: String
) {
    // Find the current route name
    val uiState by routeViewModel.state.collectAsState()
    val route = uiState.routes.find { it.id == routeId }

    var newName by remember { mutableStateOf(route?.name ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Route Name") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Route Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Logic to update just the name in Firebase could go here
                    // For now, we'll just navigate back to show it "feels" like a screen
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = newName.isNotBlank()
            ) {
                Text("Save Changes")
            }
        }
    }
}