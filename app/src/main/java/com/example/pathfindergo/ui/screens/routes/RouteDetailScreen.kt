package com.example.pathfindergo.ui.screens.routes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pathfindergo.navigation.Screen
import com.example.pathfindergo.ui.viewmodels.RouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailScreen(
    navController: NavController,
    routeViewModel: RouteViewModel,
    routeId: String
) {
    val context = LocalContext.current
    val uiState by routeViewModel.state.collectAsState()
    val waypoints by routeViewModel.currentWaypoints.collectAsState()

    // Find the specific route data from our list
    val route = uiState.routes.find { it.id == routeId }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Load waypoints when screen opens
    LaunchedEffect(routeId) {
        routeViewModel.loadRouteDetails(routeId)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Route") },
            text = { Text("Are you sure you want to delete this route? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    routeViewModel.deleteRoute(routeId) {
                        showDeleteDialog = false
                        navController.popBackStack()
                    }
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(route?.name ?: "Route Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Edit Button
                    IconButton(onClick = { navController.navigate(Screen.EditRoute.createRoute(routeId)) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    // Delete Button
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
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
        ) {
            // Stats Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Distance", style = MaterialTheme.typography.labelMedium)
                        Text("${String.format("%.2f", route?.totalDistanceKm ?: 0.0)} KM", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = {
                        val textToCopy = waypoints.joinToString("\n") { "${it.latitude}, ${it.longitude}" }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Route Coordinates", textToCopy)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Coordinates copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Copy All")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Waypoints (${waypoints.size})", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(waypoints) { wp ->
                    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${wp.order + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(32.dp)
                            )
                            Text(
                                text = "${wp.latitude}, ${wp.longitude}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}