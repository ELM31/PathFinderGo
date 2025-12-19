package com.example.pathfindergo.ui.screens.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pathfindergo.ui.viewmodels.AuthViewModel
import com.example.pathfindergo.ui.viewmodels.RouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRouteScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    routeViewModel: RouteViewModel
) {
    var routeName by remember { mutableStateOf("") }
    var rawCoordinates by remember { mutableStateOf("") }

    val uiState by routeViewModel.state.collectAsState()
    val userId = authViewModel.currentUser?.uid

    // Navigate back once the route is successfully saved
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            routeViewModel.resetSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Route") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paste your coordinates below. The app will automatically extract the points and calculate the distance.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Route Name Input
            OutlinedTextField(
                value = routeName,
                onValueChange = { routeName = it },
                label = { Text("Route Name (e.g., Central Park Loop)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bulk Coordinate Input
            OutlinedTextField(
                value = rawCoordinates,
                onValueChange = { rawCoordinates = it },
                label = { Text("Paste Coordinate Dump") },
                placeholder = { Text("34.05, -118.24\n34.06, -118.25...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp), // Large area for pasting
                supportingText = {
                    Text("Accepted formats: 'Lat, Long' or 'Lat Long'")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error Display
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Save Button logic
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (userId != null && routeName.isNotBlank() && rawCoordinates.isNotBlank()) {
                            routeViewModel.createRouteFromText(routeName, rawCoordinates, userId)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = routeName.isNotBlank() && rawCoordinates.isNotBlank()
                ) {
                    Text("Generate & Save Route")
                }
            }
        }
    }
}