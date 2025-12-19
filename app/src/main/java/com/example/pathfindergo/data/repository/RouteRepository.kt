package com.example.pathfindergo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.pathfindergo.data.models.Route
import com.example.pathfindergo.data.models.Waypoint
import kotlinx.coroutines.tasks.await

class RouteRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    // Save a new route and its points to Firestore
    suspend fun saveRoute(route: Route, waypoints: List<Waypoint>): Result<Unit> {
        return try {
            // Create a new document reference to get an ID
            val routeRef = db.collection("routes").document()
            val finalRoute = route.copy(id = routeRef.id)

            // 1. Save the Route object
            routeRef.set(finalRoute).await()

            // 2. Save each Waypoint into a "sub-collection" inside that route
            val batch = db.batch()
            waypoints.forEach { waypoint ->
                val wpRef = routeRef.collection("waypoints").document(waypoint.id)
                batch.set(wpRef, waypoint)
            }
            batch.commit().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get all routes for a specific user
    suspend fun getRoutes(userId: String): List<Route> {
        return try {
            val snapshot = db.collection("routes")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.toObjects(Route::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun getWaypoints(routeId: String): List<Waypoint> {
        return try {
            val snapshot = db.collection("routes").document(routeId)
                .collection("waypoints")
                .orderBy("order")
                .get()
                .await()
            snapshot.toObjects(Waypoint::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteRoute(routeId: String): Result<Unit> {
        return try {
            // In a real app, you'd delete sub-collections too, but for a
            // school project, deleting the main document is usually enough.
            db.collection("routes").document(routeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}