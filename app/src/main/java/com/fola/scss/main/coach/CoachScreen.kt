package com.fola.scss.main.coach

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.domain.model.Coach
import com.fola.domain.model.Service
import com.fola.domain.usecase.CoachUseCase
import com.fola.scss.assests.icons.PersonCircle
import com.fola.scss.main.users.SearchBar


@Composable
fun CoachScreen(
    modifier: Modifier = Modifier,
    viewModel: CoachViewmodel = viewModel(),
) {
    val coaches = viewModel.filteredCoaches.collectAsState().value
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                value = viewModel.searchingCoaches.collectAsState().value,
                onValueChange = { viewModel.findCoaches(it) },
                placeholder = "Search Coaches",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Coach")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        CoachListScreen(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            coaches = coaches,
            viewModel = viewModel
        )
    }
    val buttonState = viewModel.buttonState.collectAsState().value

    if (buttonState.assignTOService != null) {
        ChooseServiceDialog(
            viewmodel = viewModel,
            coach = buttonState.assignTOService,
        )
    }


}

@Composable
fun CoachListScreen(
    modifier: Modifier = Modifier,
    coaches: List<Coach>,
    viewModel: CoachViewmodel
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(coaches, key = { it.id }) { coach ->
            HorizontalSpacer()
            CoachRow(
                coach = coach,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CoachRow(
    coach: Coach,
    modifier: Modifier = Modifier,
    viewModel: CoachViewmodel
) {
    var expanded by remember { mutableStateOf(false) }



    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CoachAvatar(
                    base64 = coach.image,
                    modifier = Modifier
                        .size(56.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${coach.firstName} ${coach.lastName}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = coach.specialty,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand/Collapse",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CoachDetailRow(
                        label = "Phone",
                        value = coach.phoneNumber
                    )
                    CoachDetailRow(
                        label = "Birthday",
                        value = coach.dateOfBirth
                    )
                    CoachDetailRow(
                        label = "Salary",
                        value = "${coach.salary} EGP"
                    )
                    CoachDetailRow(
                        label = "Rating",
                        value = if (coach.avgRating > 0) {
                            "\u2605".repeat(coach.avgRating)
                        } else {
                            "No Rating"
                        },
                        valueColor = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionButton(
                            text = "Edit",
                            icon = Icons.Default.Edit,
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        )
                        ActionButton(
                            text = "Link",
                            icon = Icons.Default.Link,
                            onClick = { viewModel.setLinkedCoach(coach) },
                            modifier = Modifier.weight(1f)
                        )
                        ActionButton(
                            text = "Delete",
                            icon = Icons.Default.Delete,
                            onClick = { viewModel.deleteCoach(coach) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoachAvatar(
    base64: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (!base64.isNullOrEmpty()) {
            val imageBytes = remember(base64) {
                Base64.decode(base64, Base64.DEFAULT)
            }
            val bitmap = remember(imageBytes) {
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            }

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Coach Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonCircle,
                    contentDescription = "Default Avatar",
                    modifier = Modifier.fillMaxSize(0.6f),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun CoachDetailRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Composable
fun ChooseServiceDialog(
    modifier: Modifier = Modifier,
    viewmodel: CoachViewmodel,
    coach: Coach
) {
    val selectedService = remember { mutableStateOf<Service?>(null) }
    val services = viewmodel.services.collectAsState().value

    Dialog(
        onDismissRequest = { viewmodel.unsetLinkedCoach() },
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Choose a Service for ${coach.firstName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(services) { service ->
                        ServiceItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedService.value = service
                                }
                                .padding(2.dp),
                            s = service,
                            isSelected = service.id == selectedService.value?.id
                        )
                    }
                }

                Button(
                    onClick = {
                        selectedService.value?.let { service ->
                            viewmodel.assignCoachToService(coach, service)
                        }
                        viewmodel.unsetLinkedCoach()
                    },
                    enabled = selectedService.value != null,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Assign Service")
                }
            }
        }
    }


}

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    s: Service,
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = s.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = s.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "${s.price} EGP",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }


}

@Composable
fun HorizontalSpacer(
    modifier: Modifier = Modifier,
    height: Dp = 1.dp
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    )
}

class CoachViewModelFactory(
    private val coachUseCase: CoachUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoachViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoachViewmodel(coachUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
