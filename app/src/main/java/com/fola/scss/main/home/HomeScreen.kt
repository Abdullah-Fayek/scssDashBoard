package com.fola.scss.main.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.scss.ui.theme.AppTheme


@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val stats by viewModel.stats.collectAsState()
    val qas by viewModel.qas.collectAsState()
    val verticalScroll = rememberScrollState()


    Scaffold(
        floatingActionButton = {
            //     QuickActions()
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppointmentHomeScreen()

        }
    }
}


// Data class
data class Appointment(
    val id: Int,
    val coachName: String,
    val serviceName: String,
    val day: String,
    val time: String,
    val duration: Int,
    val maxAttenderNum: Int,
    val currentAttendance: Int = 0
)

@Composable
fun AppointmentCard(appointment: Appointment) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke((1).dp, MaterialTheme.colorScheme.outline) // light gray border
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "${appointment.serviceName} with ${appointment.coachName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D2D2D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date & Time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppointmentInfoItem(label = "Date", value = appointment.day)
                AppointmentInfoItem(label = "Time", value = appointment.time)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Duration & Attendees Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppointmentInfoItem(label = "Duration", value = "${appointment.duration} min")
                AppointmentInfoItem(
                    label = "Attendees",
                    value = "${appointment.currentAttendance}/${appointment.maxAttenderNum}"
                )
            }
        }
    }
}

@Composable
fun AppointmentInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

val appointment = Appointment(
    id = 1,
    coachName = "John Doe",
    serviceName = "Yoga Session",
    day = "Monday",
    time = "10:00 AM",
    duration = 60,
    maxAttenderNum = 10,
    currentAttendance = 5
)
@Composable
fun AppointmentHomeScreen() {
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {

                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)
                AppointmentCard(appointment = appointment)

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppointmentCardPreview() {
    AppointmentHomeScreen()
}

//
//@Composable
//fun StatsSection(stats: List<Stat>) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        Text(
//            text = "Overview",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorSchemecheme.onBackground
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp)
//                .clip(RoundedCornerShape(50))
//                .background(MaterialTheme.colorSchemecheme.outlineVariant)
//                .padding(2.dp)
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 4.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                StatCard(stats[0], Modifier.weight(.5f))
//                StatCard(stats[1], Modifier.weight(.5f))
//            }
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                StatCard(stats[2], Modifier.weight(.5f))
//                StatCard(stats[3], Modifier.weight(.5f))
//            }
//        }
//    }
//
//}
//
//@Composable
//fun StatCard(stat: Stat, modifier: Modifier = Modifier) {
//
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp)),
//        colors = CardDefaults.cardColors(
//            containerColor = stat.color.copy(0.3f),
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Start
//            ) {
//                Icon(
//                    imageVector = stat.icon,
//                    contentDescription = null,
//                    tint = stat.color,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .padding(end = 8.dp)
//                )
//                Text(
//                    text = stat.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.SemiBold,
//                )
//            }
//
//            Text(
//                text = stat.count.toString(),
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.Bold,
//                color = stat.color
//            )
//        }
//    }
//}
//
//
//@Composable
//fun AppointmentsSection(appointments: List<Appointment>) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Upcoming Appointments",
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorSchemecheme.onBackground
//            )
//            TextButton(onClick = { /* Navigate to bookings */ }) {
//                Text(
//                    text = "See All",
//                    color = MaterialTheme.colorSchemecheme.secondary,
//                    style = MaterialTheme.typography.labelLarge
//                )
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        appointments.forEach { appointment ->
//            AppointmentItem(appointment = appointment)
//        }
//    }
//}
//
//@Composable
//fun AppointmentItem(appointment: Appointment) {
//    var isHovered by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .clickable { /* Navigate to appointment details */ }
//            .shadow(if (isHovered) 6.dp else 2.dp, RoundedCornerShape(12.dp)),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorSchemecheme.surface,
//            contentColor = MaterialTheme.colorSchemecheme.onSurface
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorSchemecheme.primary.copy(alpha = 0.1f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = appointment.time.take(2),
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorSchemecheme.primary,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(
//                    text = "${appointment.time} - ${appointment.serviceId}",
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorSchemecheme.onSurface
//                )
//                Text(
//                    text = "Coach: ${appointment.time} | User: ${appointment.maxAttenderNum}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorSchemecheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun ReviewsSection(reviews: List<Review>) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        Text(
//            text = "Recent Reviews",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorSchemecheme.onBackground
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp)
//                .clip(RoundedCornerShape(50))
//                .background(MaterialTheme.colorSchemecheme.outlineVariant)
//                .padding(2.dp)
//        )
//        reviews.forEach { review ->
//            ReviewItem(review = review)
//        }
//    }
//}
//
//@Composable
//fun ReviewItem(review: Review) {
//    var isHovered by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .clickable { /* Navigate to review details */ }
//            .shadow(if (isHovered) 6.dp else 2.dp, RoundedCornerShape(12.dp)),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorSchemecheme.surface,
//            contentColor = MaterialTheme.colorSchemecheme.onSurface
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "${review.type}: ${review.name}",
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorSchemecheme.onSurface
//                )
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            Row {
//                repeat(5) { index ->
//                    Icon(
//                        imageVector = Icons.Default.Star,
//                        contentDescription = null,
//                        tint = if (index < review.rating.roundToInt()) MaterialTheme.colorSchemecheme.primary else MaterialTheme.colorSchemecheme.onSurfaceVariant.copy(
//                            alpha = 0.5f
//                        ),
//                        modifier = Modifier.size(18.dp)
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = review.comment,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorSchemecheme.onSurfaceVariant,
//                maxLines = 2
//            )
//        }
//    }
//}
//
//@Composable
//fun QAsSection(qas: List<QA>) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        Text(
//            text = "Unanswered QAs",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorSchemecheme.onBackground
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp)
//                .clip(RoundedCornerShape(50))
//                .background(MaterialTheme.colorSchemecheme.outlineVariant)
//                .padding(2.dp)
//        )
//        qas.forEach { qa ->
//            QAItem(qa = qa)
//        }
//    }
//}
//
//@Composable
//fun QAItem(qa: QA) {
//    var isHovered by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .clickable { /* Navigate to QA details */ }
//            .shadow(if (isHovered) 6.dp else 2.dp, RoundedCornerShape(12.dp)),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorSchemecheme.surface,
//            contentColor = MaterialTheme.colorSchemecheme.onSurface
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = qa.question,
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorSchemecheme.onSurface
//                )
//                Text(
//                    text = "Asked by: ${qa.user}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorSchemecheme.onSurfaceVariant
//                )
//            }
//            Button(
//                onClick = { /* Navigate to QA section */ },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorSchemecheme.secondary,
//                    contentColor = MaterialTheme.colorSchemecheme.onSecondary
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text("Respond", style = MaterialTheme.typography.labelLarge)
//            }
//        }
//    }
//}
//
//@Composable
//fun QuickActions() {
//    var expanded by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .padding(16.dp)
//            .wrapContentSize(Alignment.BottomEnd)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.End,
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            AnimatedVisibility(
//                visible = expanded,
//                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
//                    animationSpec = tween(
//                        300
//                    )
//                ),
//                exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
//                    animationSpec = tween(
//                        300
//                    )
//                )
//            ) {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    val actions = listOf(
//                        "Add New Coach",
//                        "Add Membership",
//                        "Assign Coach",
//                        "Create Service"
//                    )
//                    actions.forEach { action ->
//                        Row(
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(50))
//                                .background(MaterialTheme.colorSchemecheme.primary.copy(alpha = 0.9f))
//                                .clickable { /* Handle action */ }
//                                .padding(horizontal = 16.dp, vertical = 12.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                text = action,
//                                color = MaterialTheme.colorSchemecheme.onPrimary,
//                                style = MaterialTheme.typography.labelLarge
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Icon(
//                                Icons.Default.Add,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorSchemecheme.onPrimary,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
//                }
//            }
//            FloatingActionButton(
//                onClick = { expanded = !expanded },
//                shape = RoundedCornerShape(32),
//                containerColor = MaterialTheme.colorSchemecheme.primary,
//                contentColor = MaterialTheme.colorSchemecheme.onPrimary,
//                modifier = Modifier.size(64.dp)
//            ) {
//                Icon(
//                    Icons.Default.Add,
//                    contentDescription = "Expand actions",
//                    modifier = Modifier.size(32.dp)
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun HomePrev() {
//    DashboardScreen()
//
//}