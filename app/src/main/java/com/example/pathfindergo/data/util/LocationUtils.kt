package com.example.pathfindergo.data.util

import com.example.pathfindergo.data.models.Waypoint
import kotlin.math.*

object LocationUtils {
    fun calculateTotalDistance(points: List<Waypoint>): Double {
        if (points.size < 2) return 0.0
        var total = 0.0
        for (i in 0 until points.size - 1) {
            total += calculateHaversine(points[i], points[i+1])
        }
        return total
    }

    fun optimizePath(waypoints: List<Waypoint>): List<Waypoint> {
        if (waypoints.size <= 2) return waypoints // No need to optimize 1 or 2 points

        val unvisited = waypoints.toMutableList()
        val optimizedPath = mutableListOf<Waypoint>()

        // 1. Start with the first point the user provided
        var currentPoint = unvisited.removeAt(0)
        optimizedPath.add(currentPoint.copy(order = 0))

        // 2. Iteratively find the closest next point
        var orderCounter = 1
        while (unvisited.isNotEmpty()) {
            val nextPoint = unvisited.minByOrNull { calculateHaversine(currentPoint, it) }

            nextPoint?.let {
                unvisited.remove(it)
                optimizedPath.add(it.copy(order = orderCounter++))
                currentPoint = it
            }
        }

        return optimizedPath
    }

    private fun calculateHaversine(p1: Waypoint, p2: Waypoint): Double {
        val r = 6371 // Earth radius in KM
        val latDistance = Math.toRadians(p2.latitude - p1.latitude)
        val lonDistance = Math.toRadians(p2.longitude - p1.longitude)
        val a = sin(latDistance / 2).pow(2) +
                cos(Math.toRadians(p1.latitude)) * cos(Math.toRadians(p2.latitude)) *
                sin(lonDistance / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}