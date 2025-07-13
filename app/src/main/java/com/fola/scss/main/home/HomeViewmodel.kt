package com.fola.scss.main.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.fola.domain.model.Appointment
import com.fola.domain.model.QA
//import com.fola.domain.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Stat(val title: String, val count: Int, val icon: ImageVector, val color: Color)


class DashboardViewModel : androidx.lifecycle.ViewModel() {
    private val _stats = MutableStateFlow(
        listOf(
            Stat("Users", 1250, Icons.Default.Group, Color(0xFF2196F3)),
            Stat("Coaches", 45, Icons.Default.Person, Color(0xFF4CAF50)),
            Stat("Memberships", 320, Icons.Default.Star, Color(0xFFF44336)),
            Stat("Bookings", 18, Icons.Default.Today, Color(0xFFFF9800))
        )
    )
    private val _appointments = MutableStateFlow(
        emptyList<Appointment>()
    )

    val stats: StateFlow<List<Stat>> = _stats


    val qas: StateFlow<List<QA>> = MutableStateFlow(
        emptyList<QA>()
    )
}

