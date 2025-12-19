# PathFinder GO – README

**Contributor:** Eric Lopez Morales
**CWID:** 813485034

---

## Project Overview
**PathFinder GO** is a specialized Android utility designed for Pokémon GO players to organize, optimize, and store complex sets of GPS coordinates. Built with **Jetpack Compose** and **Firebase**, the app allows users to bulk-paste raw coordinate data, which the app then parses and reorders using a **Nearest Neighbor optimization algorithm** to create the most efficient walking path. 

The app follows the **MVVM (Model-View-ViewModel)** architecture, ensuring a clean separation between the Firebase cloud data layer.

---

## App Layout (9 Functional Screens)
This app meets the requirement of 6-8 screens by providing:
1. **Login Screen:** Secure entry point with Firebase Auth.
2. **Sign-Up Screen:** Account creation with password/email validation.
3. **Dashboard (Home):** Summary of user stats.
4. **Create Route Screen:** input utility with optimization engine.
5. **Route Library Screen:** List of all saved routes with sorting logic.
6. **Search/Filter View:** Integrated query logic.
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

## Screenshots

1. Login Page
<img width="408" height="908" alt="image" src="https://github.com/user-attachments/assets/9661ca01-e302-45f5-9fb2-b4a40035d0ce" />

2. Signup Page
<img width="409" height="908" alt="image" src="https://github.com/user-attachments/assets/3db69150-ede6-45c5-848d-46e5a942d4e2" />

3. Dashboard
<img width="408" height="902" alt="image" src="https://github.com/user-attachments/assets/45568851-b621-43d1-b872-33b777c3afc1" />

4. Create Route view
<img width="407" height="907" alt="image" src="https://github.com/user-attachments/assets/c68d2754-d296-47cf-b8fa-4448bec49982" />

5. Route Library
<img width="408" height="911" alt="image" src="https://github.com/user-attachments/assets/e73f076e-c5d4-449e-b193-b0bf3ddc13c3" />

6. Search Screen 
<img width="406" height="591" alt="image" src="https://github.com/user-attachments/assets/81d41b1c-80fb-4929-a779-03037b1d4d8b" />

7. Route Detail View
<img width="407" height="799" alt="image" src="https://github.com/user-attachments/assets/f0a353ad-46d2-49af-819c-bdbecfe8cec1" />

8. Edit Route View
<img width="408" height="349" alt="image" src="https://github.com/user-attachments/assets/f2735992-cb4d-4000-ab0a-1d6d788ba64f" />

9. Profile Settings
<img width="408" height="907" alt="image" src="https://github.com/user-attachments/assets/bb31ac19-a292-4745-8507-8a62ab9c7a1e" />

---

## Optimzation Example 

Downtown Los Angeles - uneditted
<img width="408" height="338" alt="image" src="https://github.com/user-attachments/assets/ec31b951-76d6-4b99-9544-181da7161f1f" />


Downtown Los Angeles - optimized
<img width="409" height="348" alt="image" src="https://github.com/user-attachments/assets/4810ec00-2d09-480f-beea-4e570961aab1" />

---

## Project Structure & Organization
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
