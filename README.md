# PathFinder GO – README

**Contributor:** Eric Lopez Morales
**CWID:** 813485034

---

## Project Overview
**PathFinder GO** is a specialized Android utility designed for Pokémon GO players to organize, optimize, and store complex sets of GPS coordinates. Built with **Jetpack Compose** and **Firebase**, the app allows users to bulk-paste raw coordinate data, which the app then parses and reorders using a **Nearest Neighbor optimization algorithm** to create the most efficient walking path. 

The app follows the **MVVM (Model-View-ViewModel)** architecture, ensuring a clean separation between the Firebase cloud data layer and the modern Material 3 user interface.

---

## Project Setup Guide
To run this project locally, follow these steps:

1. **Clone the Repository:**
   `git clone [Your GitHub URL]`
2. **Firebase Configuration:**
   - Create a new project in the [Firebase Console](https://console.firebase.google.com/).
   - Enable **Email/Password Authentication**.
   - Enable **Cloud Firestore** (Start in Test Mode).
   - Register the Android app using the package name `com.example.pathfindergo`.
   - Download the `google-services.json` file and place it in the `app/` directory of the project.
3. **Build & Run:**
   - Open the project in **Android Studio Ladybug** (or newer).
   - Sync Project with Gradle Files.
   - Run on an Emulator or Physical Device (API 31+ recommended).

---

## App Layout (9 Functional Screens)
This app meets the requirement of 6-8 screens by providing:
1. **Login Screen:** Secure entry point with Firebase Auth.
2. **Sign-Up Screen:** Account creation with real-time password/email validation.
3. **Dashboard (Home):** High-level summary of user stats.
4. **Create Route Screen:** Bulk-input utility with optimization engine.
5. **Route Library Screen:** List of all saved routes with sorting logic.
6. **Search/Filter View:** Integrated real-time query logic.
7. **Route Detail Screen:** Viewing waypoint lists and "Copy to Clipboard" utility.
8. **Edit Screen:** Update functionality for existing route metadata.
9. **Profile/Settings:** User management and secure Sign Out.

---

## Core Functionalities & Architecture

| Feature | Implementation |
| :--- | :--- |
| **Authentication** | Firebase Auth with persistent login and loading/error state management. |
| **Data Persistence** | **Firestore** handling two collections: `routes` and `waypoints`. |
| **Data Isolation** | User-specific data isolation ensuring users only see their own routes. |
| **Path Optimization** | Greedy (Nearest Neighbor) Algorithm solving the TSP problem. |
| **UX/UI** | Material 3, Confirmation Dialogs for deletion, and Empty/Loading states. |

---

## Technical Highlights

### 1. The Optimization 
To prevent "zigzagging" paths, the app processes raw coordinates using a Nearest Neighbor logic to reorder points based on proximity, significantly reducing total travel distance.

### 2. The Haversine Formula
Total distance is calculated using the spherical law of cosines:
$$d = 2r \arcsin\left(\sqrt{\sin^2\left(\frac{\Delta\phi}{2}\right) + \cos\phi_1\cos\phi_2\sin^2\left(\frac{\Delta\lambda}{2}\right)}\right)$$

---

## File Overview
* **`RouteViewModel.kt`**: Centralized state management using `StateFlow`.
* **`RouteRepository.kt`**: Clean abstraction of Firestore CRUD operations.
* **`LocationUtils.kt`**: Algorithmic logic for pathing and distance.
* **`NavGraph.kt`**: Type-safe navigation and protected routing.

---

## 📂 Project Structure & Organization
The project follows a modular package-by-feature structure to ensure high maintainability and clear separation of concerns:

```text
app/src/main/java/com/example/pathfindergo/
│
├── 📂 data
│   ├── 📂 models
│   │   └── Models.kt           <-- Contains 'Route' and 'Waypoint' data classes
│   ├── 📂 repository
│   │   ├── AuthRepository.kt   <-- Firebase Authentication logic (Login, SignUp, Logout)
│   │   └── RouteRepository.kt  <-- Firestore CRUD (Save, Get, Delete Routes/Waypoints)
│   └── 📂 util
│       ├── CoordinateParser.kt <-- Regex logic to extract coordinates from text
│       └── LocationUtils.kt    <-- Haversine formula and Path Optimization logic
│
├── 📂 ui
│   ├── 📂 screens
│   │   ├── 📂 auth
│   │   │   ├── LoginScreen.kt  <-- Login UI with validation
│   │   │   └── SignUpScreen.kt <-- Registration UI with real-time feedback
│   │   ├── 📂 dashboard
│   │   │   ├── HomeScreen.kt   <-- Dashboard with stats and welcome message
│   │   │   └── ProfileScreen.kt <-- User profile and account management
│   │   └── 📂 routes
│   │       ├── CreateRouteScreen.kt <-- Bulk-paste coordinate utility
│   │       ├── RouteListScreen.kt   <-- Library view with Search/Sort functionality
│   │       ├── RouteDetailScreen.kt <-- View details, Copy to Clipboard, and Delete
│   │       └── EditRouteScreen.kt   <-- Update route metadata
│   ├── 📂 theme
│   │   └── Theme.kt             <-- Material Design 3 configuration
│   └── 📂 viewmodels
│       ├── AuthViewModel.kt    <-- Manages Auth state and user sessions
│       └── RouteViewModel.kt   <-- Logic for parsing, optimization, and CRUD
│
├── 📂 navigation
│   ├── NavGraph.kt             <-- Navigation graph and protected route logic
│   └── Screen.kt               <-- Type-safe route definitions
│
└── MainActivity.kt             <-- App entry point and theme
