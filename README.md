# PathFinder GO – README

**Contributors:** Eric Lopez Morales

---

##Project Overview
**PathFinder GO** is a specialized Android utility designed for Pokémon GO players to organize, optimize, and store complex sets of GPS coordinates. Built with **Jetpack Compose** and **Firebase**, the app allows users to bulk-paste raw coordinate data, which the app then parses and reorders using a **Nearest Neighbor optimization algorithm** to create the most efficient walking path. 

The app follows the **MVVM (Model-View-ViewModel)** architecture, ensuring a clean separation between the Firebase cloud data layer and the modern Material 3 user interface.

---

##App Layout
The application consists of 9 distinct functional screens/states to provide a full CRUD (Create, Read, Update, Delete) experience:

1. **Login Screen:** Secure entry point using Firebase Authentication.
2. **Sign-Up Screen:** Account creation with real-time password validation.
3. **Dashboard (Home):** High-level summary displaying total routes and total distance (KM) across all saved paths.
4. **Create Route Screen:** A bulk-input utility that handles raw text parsing and triggers the optimization engine.
5. **Route Library Screen:** A scrollable list of all saved routes synced from the cloud.
6. **Search & Filter View:** Integrated search logic within the library to query routes by name or sort by distance/date.
7. **Route Detail Screen:** Displays the optimized waypoint list and includes a "Copy to Clipboard" utility.
8. **Edit Route Screen:** Allows users to update the metadata (naming) of existing routes.
9. **Profile/Settings Screen:** Manages user identity and provides secure sign-out functionality.

---

##Core Functionalities & Key Architectural Concepts

| Feature | Implementation Concept |
| :--- | :--- |
| **Cloud-Sync Architecture** | Uses **Firebase Firestore** as a real-time remote data source for cross-device access. |
| **Path Optimization** | Implements a **Greedy (Nearest Neighbor) Algorithm** to solve the Traveling Salesperson Problem (TSP). |
| **Bulk Coordinate Parsing** | A custom Regex-based parser that extracts Lat/Long pairs from unstructured text dumps. |
| **Geospatial Math** | Uses the **Haversine Formula** to calculate precise distances between coordinates on a sphere. |
| **Material 3 Design** | Utilizes modern UI components like Scaffold, Filter Chips, and Elevated Cards. |

---

##Technical Highlights: The "Engine"

### 1. Path Optimization Algorithm
To prevent "zigzagging" paths, the app processes raw coordinates using a Nearest Neighbor logic:
* **Step 1:** Start at the first coordinate provided.
* **Step 2:** Search the remaining list for the coordinate with the smallest Haversine distance.
* **Step 3:** Move to that point and repeat until all points are ordered.

### 2. The Haversine Formula
The total distance of a route is calculated using the following formula:

$$d = 2r \arcsin\left(\sqrt{\sin^2\left(\frac{\Delta\phi}{2}\right) + \cos\phi_1\cos\phi_2\sin^2\left(\frac{\Delta\lambda}{2}\right)}\right)$$

> *Where **r** is the Earth's radius (6371 km), **φ** is latitude, and **λ** is longitude.*

---

##File Overview

### Data Layer
* **`RouteRepository.kt`**: Handles all Firestore operations (Get/Save/Delete).
* **`AuthRepository.kt`**: Manages Firebase Authentication sessions.
* **`CoordinateParser.kt`**: Logic for turning raw strings into `Waypoint` objects.
* **`LocationUtils.kt`**: Contains the Optimization Algorithm and Distance math.

### UI & ViewModels
* **`AuthViewModel.kt`**: Manages the state of the user session (Loading, Error, Authenticated).
* **`RouteViewModel.kt`**: The central "Brain" that coordinates parsing, optimization, and database updates.
* **`NavGraph.kt`**: Defines the 9-screen navigation architecture and argument passing.

---

##Functions and Concepts Used
* **Jetpack Compose:** Declarative UI for a highly responsive user experience.
* **Kotlin Coroutines:** Used for non-blocking database and network calls.
* **StateFlow:** Reactive state management to ensure the UI updates instantly.
* **Firebase SDK:** Auth for security and Firestore for NoSQL cloud storage.
