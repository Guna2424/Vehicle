package com.guna.vehicle.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("homescreen")
    object AddVehicleScreen : Screen("add_vehicle_screen")
}
