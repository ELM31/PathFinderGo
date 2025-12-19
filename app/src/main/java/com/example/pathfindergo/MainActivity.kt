package com.example.pathfindergo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pathfindergo.navigation.NavGraph
import com.example.pathfindergo.ui.theme.PathFinderGoTheme
import com.example.pathfindergo.ui.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keeps the app drawing under the status bar (modern look)
        enableEdgeToEdge()

        setContent {
            // Keep your Theme for Material Design 3 requirements
            PathFinderGoTheme {
                val navController = rememberNavController()

                // Initialize the AuthViewModel
                val authViewModel: AuthViewModel = viewModel()

                // Scaffold provides a structure for your app (like top bars)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // We pass the innerPadding to our NavGraph so content doesn't
                    // get cut off by the camera notch or navigation bar
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}