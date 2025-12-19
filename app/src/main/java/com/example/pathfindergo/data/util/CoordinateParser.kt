package com.example.pathfindergo.data.util

import com.example.pathfindergo.data.models.Waypoint

object CoordinateParser {

    /**
     * Parses a string for coordinate pairs.
     * Supports formats like:
     * "34.0522, -118.2437"
     * "34.0522 -118.2437"
     * "Lat: 34.0522, Long: -118.2437"
     */
    fun parseTextToWaypoints(input: String): List<Waypoint> {
        val waypoints = mutableListOf<Waypoint>()

        // Regex pattern to find two decimal numbers separated by a comma, space, or pipe
        // It accounts for negative signs as well
        val regex = Regex("""(-?\d+\.\d+)\s*[,|\s]\s*(-?\d+\.\d+)""")

        val matches = regex.findAll(input)

        matches.forEachIndexed { index, matchResult ->
            val lat = matchResult.groupValues[1].toDoubleOrNull()
            val lng = matchResult.groupValues[2].toDoubleOrNull()

            if (lat != null && lng != null) {
                waypoints.add(
                    Waypoint(
                        latitude = lat,
                        longitude = lng,
                        order = index
                    )
                )
            }
        }
        return waypoints
    }
}