

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
//                color = MaterialTheme.colorScheme.onBackground
//            )
//            TextButton(onClick = { /* Navigate to bookings */ }) {
//                Text(
//                    text = "See All",
//                    color = MaterialTheme.colorScheme.secondary,
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
//fun ReviewsSection(reviews: List<Review>) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//    ) {
//        Text(
//            text = "Recent Reviews",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.onBackground
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp)
//                .clip(RoundedCornerShape(50))
//                .background(MaterialTheme.colorScheme.outlineVariant)
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
//            containerColor = MaterialTheme.colorScheme.surface,
//            contentColor = MaterialTheme.colorScheme.onSurface
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
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            Row {
//                repeat(5) { index ->
//                    Icon(
//                        imageVector = Icons.Default.Star,
//                        contentDescription = null,
//                        tint = if (index < review.rating.roundToInt()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
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
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
//            color = MaterialTheme.colorScheme.onBackground
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp)
//                .clip(RoundedCornerShape(50))
//                .background(MaterialTheme.colorScheme.outlineVariant)
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
//            containerColor = MaterialTheme.colorScheme.surface,
//            contentColor = MaterialTheme.colorScheme.onSurface
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
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = "Asked by: ${qa.user}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//            Button(
//                onClick = { /* Navigate to QA section */ },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.secondary,
//                    contentColor = MaterialTheme.colorScheme.onSecondary
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
//                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
//                                .clickable { /* Handle action */ }
//                                .padding(horizontal = 16.dp, vertical = 12.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                text = action,
//                                color = MaterialTheme.colorScheme.onPrimary,
//                                style = MaterialTheme.typography.labelLarge
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Icon(
//                                Icons.Default.Add,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.onPrimary,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
//                }
//            }
//            FloatingActionButton(
//                onClick = { expanded = !expanded },
//                shape = RoundedCornerShape(32),
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary,
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
//            containerColor = MaterialTheme.colorScheme.surface,
//            contentColor = MaterialTheme.colorScheme.onSurface
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
//                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = appointment.time.take(2),
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.primary,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(
//                    text = "${appointment.time}",
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = "Coach: ${appointment.time} | User: ${appointment.maxAttenderNum}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}