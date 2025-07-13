package com.fola.scss.basic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.ui.graphics.vector.ImageVector


sealed class SideDrawerScreen(val title: String, val route: String, val icon: ImageVector) {
    data object AppointmentsScreen : SideDrawerScreen("Appointments" , "APPOINTMENT", icon = Icons.Filled.DateRange)
    data object ServicesScreen : SideDrawerScreen("Services", "SERVICES", icon = Icons.Default.SportsGymnastics)

}