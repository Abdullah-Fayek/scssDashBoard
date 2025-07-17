package com.fola.scss.main.coach

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fola.domain.model.Coach
import com.fola.domain.model.CoachReviews
import com.fola.domain.model.Service
import com.fola.domain.usecase.CoachUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date

data class ButtonState(
    val isEditable: Coach? = null,
    val assignTOService: Coach? = null,
    val searchedCoach: Coach? = null,
    val deleteCoach: Coach? = null,
    val addingCoach: Coach? = null,
)

data class FieldState(
    var value: String = "",
    var isError: Boolean = false,
    var errorMessage: String = ""
)

data class FormFields(
    val firstName: FieldState = FieldState(),
    val lastName: FieldState = FieldState(),
    val phoneNumber: FieldState = FieldState(),
    val salary: FieldState = FieldState(),
    val specialty: FieldState = FieldState(),
    val showingDatePicker: Boolean = false,
    val isLoading: Boolean = false,
)

class CoachViewmodel(
    private val coachUseCase: CoachUseCase
) : ViewModel() {

    private val _coaches = MutableStateFlow<MutableList<Coach>>(mutableListOf())
    val coaches = _coaches.asStateFlow()

    val searchingCoaches = MutableStateFlow("")

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _service = MutableStateFlow<Service?>(null)
    val service: StateFlow<Service?> = _service.asStateFlow()

    private val _buttonState = MutableStateFlow(ButtonState())
    val buttonState: StateFlow<ButtonState> = _buttonState.asStateFlow()

    private val _coachReviews = MutableStateFlow<List<CoachReviews>>(emptyList())
    val coachReviews = _coachReviews.asStateFlow()

    private val snackbar = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = snackbar.asStateFlow()

    private val _formFields = MutableStateFlow(FormFields())
    val formFields: StateFlow<FormFields> = _formFields.asStateFlow()

    private val defaultBio = "Not provided"

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initCoaches()
        }
    }

    private suspend fun initCoaches() {
        _coaches.value =
            coachUseCase.getAllCoaches().getOrNull()?.toMutableList() ?: mutableListOf()
    }

    private fun showSnackbar(message: String) {
        snackbar.value = message
    }

    private fun saveEditCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            coachUseCase.editCoach(coach).onSuccess {
                updateCoachLocally(coach)
                resetButtonState()
                showSnackbar("Coach updated successfully")
                resetFormFields()
            }.onFailure {
                showSnackbar(it.localizedMessage ?: "Error updating coach")
            }
        }
    }

    fun setEditableCoach(coach: Coach) {
        _buttonState.value = _buttonState.value.copy(
            isEditable = coach,
            assignTOService = null,
            searchedCoach = null,
            deleteCoach = null,
            addingCoach = null
        )
        setFieldsValues(coach)
    }

    fun resetButtonState() {
        _buttonState.value = ButtonState()
        resetFormFields()
    }

    fun deleteCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            coachUseCase.removeCoach(coach.id).onSuccess {
                _coaches.value =
                    _coaches.value.filterNot { it.id == coach.id } as MutableList<Coach>
            }.onFailure {
                showSnackbar(it.localizedMessage ?: "Error deleting coach")
            }
        }
    }

    private fun saveAddingCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            coachUseCase.addCoach(coach).onSuccess {
                resetButtonState()
                showSnackbar("Coach added successfully")
                resetFormFields()
                _coaches.value = (_coaches.value + coach).toMutableList()
            }.onFailure {
                Log.e("CoachViewmodel", "Error adding coach: ${it.localizedMessage}")
                showSnackbar("Error adding coach")
            }
        }
    }

    fun setAddingCoach() {
        val coach = Coach(
            id = 0,
            firstName = "",
            lastName = "",
            bio = defaultBio,
            specialty = "",
        )

        _buttonState.value = _buttonState.value.copy(
            addingCoach = coach,
            isEditable = null,
            assignTOService = null,
            searchedCoach = null,
            deleteCoach = null
        )
    }

    fun findCoachById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _coaches.value.find { it.id == id }
                ?: coachUseCase.getCoach(id).getOrNull()
                    ?.also {
                        _buttonState.value = _buttonState.value.copy(
                            searchedCoach = it,
                        )
                    }
                ?: run {
                    showSnackbar("Coach not found")
                    null
                }
        }
    }

    fun setLinkedCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            _services.value = coachUseCase.getAllServices().getOrNull() ?: emptyList()
        }
        Log.d("CoachViewmodel", "Setting linked coach: ${_services.value}")
        _buttonState.value = _buttonState.value.copy(assignTOService = coach)
    }

    fun unsetLinkedCoach() {
        _buttonState.value = _buttonState.value.copy(assignTOService = null)
        _service.value = null
        _services.value = emptyList()
    }

    fun assignCoachToService(coach: Coach, service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = service.id ?: return@launch
            coachUseCase.assignCoachToService(coach.id, id).onSuccess {
                unsetLinkedCoach()
                showSnackbar("Coach assigned to service successfully")
                initCoaches()
            }.onFailure {
                val message =
                    if (it.localizedMessage?.contains("Coach is already assigned") == true) {
                        "This coach is already assigned to the selected service."
                    } else {
                        "Failed to assign coach to service. Please try again."
                    }
                showSnackbar(message)
                unsetLinkedCoach()
            }
        }
    }

    private fun updateCoachLocally(updatedCoach: Coach) {
        _coaches.value = _coaches.value.map {
            if (it.id == updatedCoach.id) updatedCoach else it
        } as MutableList<Coach>
    }

    private val filteredCoachesInternal = combine(_coaches, searchingCoaches) { coaches, query ->
        if (query.isBlank()) {
            coaches
        } else {
            coaches.filter {
                it.firstName.contains(query, ignoreCase = true) ||
                        it.lastName.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredCoaches: StateFlow<List<Coach>> = filteredCoachesInternal

    fun findCoaches(query: String) {
        searchingCoaches.value = query
    }

    fun saveFormCoach(coach: Coach) {
        Log.d("CoachViewmodel", "Saving form coach: $coach")
        _formFields.value = _formFields.value.copy(isLoading = true)

        val c = saveFieldsValues(coach)
        Log.d("CoachViewmodel", "Form fields after saving: $c")

        if (validateFormFields(c)) {
            viewModelScope.launch {
                try {
                    if (_buttonState.value.addingCoach != null) {
                        saveAddingCoach(c)
                    } else if (_buttonState.value.isEditable != null) {
                        saveEditCoach(c)
                    }
                } catch (e: Exception) {
                    Log.e("CoachViewmodel", "Error saving coach: ${e.localizedMessage}")
                    showSnackbar("Error saving coach: ${e.localizedMessage}")
                } finally {
                    _formFields.value = _formFields.value.copy(isLoading = false)
                }
            }
        } else {
            showSnackbar("Please fill in all required fields correctly.")
            _formFields.value = _formFields.value.copy(isLoading = false)
        }
    }

    fun setFirstName(name: String) {
        val current = _formFields.value
        _formFields.value = current.copy(
            firstName = current.firstName.copy(value = name, isError = false, errorMessage = "")
        )
    }

    fun setLastName(name: String) {
        val current = _formFields.value
        _formFields.value = current.copy(
            lastName = current.lastName.copy(value = name, isError = false, errorMessage = "")
        )
    }

    fun setPhoneNumber(phoneNumber: String) {
        val current = _formFields.value
        _formFields.value = current.copy(
            phoneNumber = current.phoneNumber.copy(
                value = phoneNumber,
                isError = false,
                errorMessage = ""
            )
        )
    }

    fun setSalary(salary: String) {
        val current = _formFields.value
        _formFields.value = current.copy(
            salary = current.salary.copy(value = salary, isError = false, errorMessage = "")
        )
    }



    fun setSpecialty(specialty: String) {
        val current = _formFields.value
        _formFields.value = current.copy(
            specialty = current.specialty.copy(
                value = specialty,
                isError = false,
                errorMessage = ""
            )
        )
    }

    fun toggleDatePickerVisibility(v: Boolean = false) {
        _formFields.value = _formFields.value.copy(showingDatePicker = v)
    }

    fun setLoadingState(isLoading: Boolean) {
        _formFields.value = _formFields.value.copy(isLoading = isLoading)
    }

    fun resetFormFields() {
        _formFields.value = FormFields()
    }

    fun validateFormFields(coach: Coach): Boolean {
        var isValid = true

        if (coach.firstName.isBlank()) {
            _formFields.value = _formFields.value.copy(
                firstName = FieldState(isError = true, errorMessage = "First name is required")
            )
            isValid = false
        }

        if (coach.lastName.isBlank()) {
            _formFields.value = _formFields.value.copy(
                lastName = FieldState(isError = true, errorMessage = "Last name is required")
            )
            isValid = false
        }


        if (coach.specialty.isBlank()) {
            _formFields.value = _formFields.value.copy(
                specialty = FieldState(isError = true, errorMessage = "Specialty is required")
            )
            isValid = false
        }

        return isValid
    }

    private fun setFieldsValues(coach: Coach) {
        _formFields.value = FormFields(
            firstName = FieldState(value = coach.firstName),
            lastName = FieldState(value = coach.lastName),
            specialty = FieldState(value = coach.specialty)
        )
    }

    private fun saveFieldsValues(coach: Coach): Coach {
        return coach.copy(
            firstName = _formFields.value.firstName.value,
            lastName = _formFields.value.lastName.value,
            specialty = _formFields.value.specialty.value,
            bio = defaultBio,
        )
    }

    fun resetSnackbarMessage() {
        snackbar.value = ""
    }
}
