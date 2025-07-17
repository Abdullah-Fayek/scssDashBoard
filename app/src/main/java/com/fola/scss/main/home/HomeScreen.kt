package com.fola.scss.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.domain.model.QA
import com.fola.domain.model._Appointment
import com.fola.scss.ui.theme.AppTheme



@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {


    val qas = viewModel.unansweredQAs.collectAsState().value
    val stats = viewModel.states.collectAsState().value
    val qa = viewModel.qa.collectAsState().value
    val appointments = viewModel.appointments.collectAsState().value
    val snackbarMessage = viewModel.snackbarMessage.collectAsState().value





    Scaffold(
        floatingActionButton = {
        },
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                StatsSection(stats = stats)
            }

            item {
                SectionHeader(header = "Upcoming Appointments")
            }

            items(appointments) { appointment ->
                AppointmentCard(appointment = appointment)
                if (appointment != appointments.last()) {
                    InlineSpacer()
                }
            }


            item {
                SectionHeader(header = "Frequently Asked Questions")
            }

            items(qas) { qa ->
                QAsCard(qa = qa, onClick = {viewModel.setAnsweredQA(qa)})
                if (qa != qas.last()) {
                    InlineSpacer()
                }
            }
        }
    }
    if (qa != null) {
        AnswerQuestionDialog(
            qa = qa,
            onDisRequest = { viewModel.clearAnsweredQA() },
            onSubmit = { viewModel.answerQA(it) },
            onValueChange = { viewModel.setQAAnswer(it) }
        )
    }
}


@Composable
fun AppointmentCard(appointment: _Appointment) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = "${appointment.serviceName} with ${appointment.coachName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppointmentInfoItem(label = "Date", value = appointment.day)
                AppointmentInfoItem(label = "Time", value = appointment.time)
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


@Composable
fun QAsCard(
    qa: QA,
    onClick: (QA) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = qa.question,
            modifier = modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        FilledTonalButton(
            onClick = { onClick(qa) },
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 4.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.filledTonalButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = "Answer",
            )
        }
    }
}

@Composable
fun StatsSection(stats: States) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(stats.members, Modifier.weight(.5f))
                StatCard(stats.coaches, Modifier.weight(.5f))
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(stats.services, Modifier.weight(.5f))
                StatCard(stats.appointments, Modifier.weight(.5f))
            }
        }
    }

}

@Composable
fun StatCard(stat: Stat, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = stat.color.copy(0.3f),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = stat.icon,
                    contentDescription = null,
                    tint = stat.color,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stat.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text = stat.count.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = stat.color
            )
        }
    }
}

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    header: String = ""
) {
    Row(
        modifier = modifier
            .padding(top = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(36.dp)
                .clip(RoundedCornerShape(10))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = header,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )

        }
    }
}


@Composable
fun InlineSpacer(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    )
}

@Composable
fun SeparatorLine(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(2.dp)
    )

}

@Composable
fun AnswerQuestionDialog(
    modifier: Modifier = Modifier,
    qa: QA,
    onDisRequest: () -> Unit,
    onSubmit: (QA) -> Unit,
    onValueChange: (String) -> Unit = {}
) {
    Dialog(onDismissRequest = { onDisRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = qa.question,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = qa.answer ?: "",
                    onValueChange = { onValueChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Type your answer here...") },
                    maxLines = 5,
                    singleLine = false,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { onDisRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onSubmit(qa) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun HomePrev() {
    AppTheme(darkTheme = false) {
        DashboardScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionHeaderPrev() {
    AppTheme(darkTheme = true) {
        DashboardScreen()
    }
}

