package com.example.pathfindergo.data.models
import com.google.firebase.firestore.DocumentId
import java.util.UUID

// The parent "Folder" for a list of points
data class Route(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val totalPoints: Int = 0,
    val totalDistanceKm: Double = 0.0,
    val isPublic: Boolean = false
)

// The individual GPS points
data class Waypoint(
    val id: String = UUID.randomUUID().toString(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val order: Int = 0 // To maintain the sequence of the path
)