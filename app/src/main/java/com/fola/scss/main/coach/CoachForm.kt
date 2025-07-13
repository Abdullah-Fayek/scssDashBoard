/*
package com.fola.scss.main.coach

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fola.domain.model.Coach
import com.fola.scss.ui.theme.AppTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoachForm(
    modifier: Modifier = Modifier,
    viewmodel: CoachFormViewmodel = CoachFormViewmodel()
) {
    val coach = list[0]
    val isEditing = viewmodel.coachForm.collectAsState().value.isEditable

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Edit Coach" else "View Coach",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isEditing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton(
                        text = "Save",
                        icon = Icons.Default.Save,
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = "Reset",
                        icon = Icons.Default.Refresh,
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = "Assign",
                        icon = Icons.Default.Link,
                        onClick = { */
/* TODO: Assign to service *//*
 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { paddingValues ->
        CoachFormContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            coach = coach,
            isEditing = isEditing,
            onEditCLick = { viewmodel.isEditable() },
            onCoachChange = { updatedCoach ->
            }
        )
    }
}


@Composable
fun CoachFormContent(
    modifier: Modifier = Modifier,
    coach: Coach,
    isEditing: Boolean,
    onEditCLick: () -> Unit = {},
    onCoachChange: (Coach) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // Form validation states
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var specialtyError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var birthdayError by remember { mutableStateOf<String?>(null) }
    var salaryError by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        item {
            CoachAvatarProfile(
                base64 = null,
                modifier = Modifier.size(180.dp),
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedButton(
                    onClick = { onEditCLick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green.copy(alpha = .4f),
                        contentColor = Color.Black
                    )
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Edit")

                    }
                }

                OutlinedButton(onClick = {  }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Assign Coach",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Assign")

                    }
                }
                OutlinedButton(
                    onClick = { TODO("Assign coach to service") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )

                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Delete")

                    }
                }
            }
        }
        item {
            EditableField(
                label = "First Name",
                value = coach.firstName,
                isEditable = isEditing,
                error = firstNameError,
                onValueChange = { value ->
                    firstNameError = if (value.isBlank()) "First name is required" else null
                    onCoachChange(coach.copy(firstName = value))
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
        }

        item {
            EditableField(
                label = "Last Name",
                value = coach.lastName,
                isEditable = isEditing,
                error = lastNameError,
                onValueChange = { value ->
                    lastNameError = if (value.isBlank()) "Last name is required" else null
                    onCoachChange(coach.copy(lastName = value))
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
        }

        item {
            EditableField(
                label = "Specialty",
                value = coach.specialty,
                isEditable = isEditing,
                error = specialtyError,
                onValueChange = { value ->
                    specialtyError = if (value.isBlank()) "Specialty is required" else null
                    onCoachChange(coach.copy(specialty = value))
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
        }

        item {
            EditableField(
                label = "Phone",
                value = coach.phoneNumber,
                isEditable = isEditing,
                error = phoneError,
                onValueChange = { value ->
                    phoneError = if (!value.matches(Regex("^[0-9]{10,13}\$"))) {
                        "Invalid phone number"
                    } else null
                    onCoachChange(coach.copy(phoneNumber = value))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
        }

        item {
            EditableField(
                label = "Birthday",
                value = coach.dateOfBirth,
                isEditable = isEditing,
                error = birthdayError,
                onValueChange = { value ->
                    birthdayError = if (!value.matches(Regex("^\\d{2}/\\d{2}/\\d{4}\$"))) {
                        "Invalid date format (DD/MM/YYYY)"
                    } else null
                    onCoachChange(coach.copy(dateOfBirth = value))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onClick = { if (isEditing) showDatePicker = true }
            )
        }

        item {
            EditableField(
                label = "Salary (EGP)",
                value = coach.salary.toString(),
                isEditable = isEditing,
                error = salaryError,
                onValueChange = { value ->
                    salaryError = if (value.toIntOrNull() == null || value.toInt() <= 0) {
                        "Invalid salary"
                    } else null
                    onCoachChange(coach.copy(salary = value.toIntOrNull() ?: coach.salary))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        if (!isEditing) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Reviews",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.background,
                        tonalElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "This coach is amazing!",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Appointments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "07/12/2025 - Yoga Session",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "08/12/2025 - Fitness Training",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                onCoachChange(coach.copy(dateOfBirth = date))
                showDatePicker = false
            }
        )
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    isEditable: Boolean,
    error: String?,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            readOnly = !isEditable,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = isEditable,
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            leadingIcon = {
                Icon(
                    imageVector = when (label) {
                        "First Name", "Last Name" -> Icons.Default.Person
                        "Specialty" -> Icons.Default.Work
                        "Phone" -> Icons.Default.Phone
                        "Birthday" -> Icons.Default.Cake
                        "Salary (EGP)" -> Icons.Default.Money
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = keyboardOptions,
            isError = error != null
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun CoachAvatarProfile(
    base64: String?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Box(
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
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Avatar",
                        modifier = Modifier.fillMaxSize(0.6f),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }


        }
    }
}

@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        year,
        month,
        day
    )
        .apply {
            setOnCancelListener { onDismiss() }
            setOnDismissListener { onDismiss() }
            show()
        }


}

*/
