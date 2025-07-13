package com.fola.scss.main.coach

import androidx.lifecycle.ViewModel
import com.fola.domain.model.Coach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class FormUiState(
    val isEditable: Boolean = false,
    val assignTOService: Boolean = false,
    val deleteCoach: Boolean = false,

    )

class CoachFormViewmodel : ViewModel() {

    val _coachForm = MutableStateFlow(FormUiState())
    val coachForm: StateFlow<FormUiState> = _coachForm.asStateFlow()

    val _coach = MutableStateFlow<Coach?>(null)
    val coach: StateFlow<Coach?> = _coach.asStateFlow()


    fun setCoach(coach: Coach) {
        _coach.value = coach
    }

    fun isEditable() {
        _coachForm.value = _coachForm.value.copy(isEditable = !_coachForm.value.isEditable)
    }

    fun assignToService(assignTOService: Boolean) {
        _coachForm.value = _coachForm.value.copy(assignTOService = assignTOService)
    }

    fun deleteCoach(deleteCoach: Boolean) {
        _coachForm.value = _coachForm.value.copy(deleteCoach = deleteCoach)
    }

    fun setFirstName(name: String) {
        _coach.value = _coach.value?.copy(firstName = name)
    }

    fun setLastName(name: String) {
        _coach.value = _coach.value?.copy(lastName = name)
    }

    fun setPhoneNumber(email: String) {
        _coach.value = _coach.value?.copy(phoneNumber = email)
    }

    fun setBio(bio: String) {
        _coach.value = _coach.value?.copy(bio = bio)
    }

    fun setSalary(salary: Int) {
        _coach.value = _coach.value?.copy(salary = salary)
    }


    fun AddNewCoach() {
        // Logic to add a new coach
        // This could involve calling a use case or repository method to save the coach data
    }

    fun UpdateCoach() {
        // Logic to update the existing coach
        // This could involve calling a use case or repository method to update the coach data
    }

    fun DeleteCoach() {
        // Logic to delete the coach
        // This could involve calling a use case or repository method to delete the coach data
    }


}