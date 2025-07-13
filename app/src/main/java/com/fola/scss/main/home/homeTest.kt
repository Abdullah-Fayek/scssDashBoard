package com.fola.scss.main.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fola.scss.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreenTest(
    modifier: Modifier = Modifier,
    onViewAllUsers: () -> Unit = {},
    onViewAllCoaches: () -> Unit = {},
    onViewAllMemberships: () -> Unit = {},
    onViewAllBookings: () -> Unit = {},
    onRespondQA: (String) -> Unit = {}
) {
    // Simulated API data
    val totalUsers = 150
    val totalCoaches = 25
    val totalMemberships = 200
    val todaysBookings = 12
    val upcomingAppointments = listOf(
        Triple("10:00 AM", "Yoga Session", "Coach John"),
        Triple("11:30 AM", "Personal Training", "Coach Jane")
    )
    val recentReviews = listOf(
        Triple("Club Review", 4.5f, "Great facilities!"),
        Triple("Coach Review", 5.0f, "Amazing trainer!")
    )
    val unansweredQAs = listOf(
        Pair("How do I cancel?", "User1"),
        Pair("Session timing?", "User2")
    )
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background.copy(alpha = 0.85f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    // Main Scaffold
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Stats Section with Staggered Layout
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                     //   .graphicsLayer { translationY = animateFloatAsState(targetValue = 0f, animationSpec = spring()).value }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatsCard(
                            icon = Icons.Default.People,
                            title = "Total Users",
                            count = totalUsers,
                            onClick = onViewAllUsers
                        )
                        StatsCard(
                            icon = Icons.Default.Person,
                            title = "Total Coaches",
                            count = totalCoaches,
                            onClick = onViewAllCoaches
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatsCard(
                            icon = Icons.Default.CardMembership,
                            title = "Total Memberships",
                            count = totalMemberships,
                            onClick = onViewAllMemberships
                        )
                        StatsCard(
                            icon = Icons.Default.CalendarToday,
                            title = "Today’s Bookings",
                            count = todaysBookings,
                            onClick = onViewAllBookings
                        )
                    }
                }

                // Upcoming Appointments with Parallax Effect
                Text(
                    text = "Upcoming Appointments",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                   //     .graphicsLayer { translationY = animateFloatAsState(targetValue = 0f, animationSpec = spring()).value }
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .graphicsLayer {
                        //    translationY = animateFloatAsState(targetValue = 0f, animationSpec = spring()).value.toFloat() * 0.1f
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        upcomingAppointments.forEachIndexed { index, (time, service, coach) ->
                            Text(
                                text = "$time - $service with $coach",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .graphicsLayer { alpha = 1f - (index * 0.1f) }
                            )
                        }
                        TextButton(
                            onClick = onViewAllBookings,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "See All",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.scale(animateFloatAsState(targetValue = 1f, animationSpec = spring()).value)
                            )
                        }
                    }
                }

                // Recent Reviews with Hover Effect
                Text(
                    text = "Recent Reviews",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                   //     .graphicsLayer { translationY = animateFloatAsState(targetValue = 0f, animationSpec = spring()).value }
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(12.dp)),
                      //  .hoverable { /* Add subtle scale effect */ },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        recentReviews.forEach { (type, rating, comment) ->
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .clickable { /* View full review */ },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$type - $rating⭐",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = comment,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                // Unanswered QAs with Dynamic Buttons
                Text(
                    text = "Unanswered QAs",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                      //  .graphicsLayer { translationY = animateFloatAsState(targetValue = 0f, animationSpec = spring()).value }
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        unansweredQAs.forEach { (question, user) ->
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .clickable { onRespondQA(question) },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$question (by $user)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Button(
                                    onClick = { onRespondQA(question) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.scale(animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value)
                                ) {
                                    Text("Respond", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                // Quick Actions with Animated Entrance
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp, top = 16.dp)
                      //  .graphicsLayer { translationY = animateFloatAsState(targetValue = 0f, animationSpec = spring()).value }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FloatingActionButton(
                        onClick = { /* Add New Coach */ },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.scale(animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Coach")
                    }
                    FloatingActionButton(
                        onClick = { /* Add Membership */ },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.scale(animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value)
                    ) {
                        Icon(Icons.Default.CardMembership, contentDescription = "Add Membership")
                    }
                    FloatingActionButton(
                        onClick = { /* Assign Coach to Service */ },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.scale(animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = "Assign Coach")
                    }
                    FloatingActionButton(
                        onClick = { /* Create Service */ },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.scale(animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create Service")
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    icon: ImageVector,
    title: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
         //   .hoverable { /* Subtle scale effect */ }
//            .graphicsLayer {
//                scaleX = animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value
//                scaleY = animateFloatAsState(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)).value
//            }
,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$title Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Preview
@Composable
private fun HomeTest() {
    AppTheme {
        DashboardScreenTest()
    }
    
}
@Preview
@Composable
private fun HomeTestDark() {
    AppTheme (darkTheme = true){
        DashboardScreenTest()
    }

}