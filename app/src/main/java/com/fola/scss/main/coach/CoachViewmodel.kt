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
import kotlinx.coroutines.withContext


data class buttonState(
    val isEditable: Coach? = null,
    val assignTOService: Coach? = null,
    val searchedCoach: Coach? = null,
    val deleteCoach: Coach? = null
)

class CoachViewmodel(
    private val coachUseCase: CoachUseCase
) : ViewModel() {

    private val _coaches = MutableStateFlow<List<Coach>>(emptyList())
    val coaches = _coaches.asStateFlow()

    val searchingCoaches = MutableStateFlow("")

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _service = MutableStateFlow<Service?>(null)
    val service: StateFlow<Service?> = _service.asStateFlow()

    private val _buttonState = MutableStateFlow(buttonState())
    val buttonState: StateFlow<buttonState> = _buttonState.asStateFlow()

    private val _coachReviews = MutableStateFlow<List<CoachReviews>>(emptyList())
    val coachReviews = _coachReviews.asStateFlow()

    private val snackbar = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = snackbar.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initCoaches()
        }

    }


    private suspend fun initCoaches() {
        _coaches.value = coachUseCase.getAllCoaches().getOrNull() ?: emptyList()
    }


    fun editCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            coachUseCase.editCoach(coach).onSuccess {
                initCoaches()
            }.onFailure {
                snackbar.value = it.localizedMessage ?: "Error updating coach"
            }


        }
    }

    fun deleteCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            coachUseCase.removeCoach(coach.id).onSuccess {
                initCoaches()
            }.onFailure {
                snackbar.value = it.localizedMessage ?: "Error deleting coach"

            }
        }
    }

    fun addCoach(coach: Coach) {
        viewModelScope.launch(Dispatchers.IO) {
            coachUseCase.addCoach(coach).onSuccess {
                initCoaches()
            }.onFailure {
                snackbar.value = it.localizedMessage ?: "Error adding coach"
            }
        }
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
                    snackbar.value = "Coach not found"
                    null
                }
        }
    }


    fun setLinkedCoach(coach: Coach) {

        viewModelScope.launch(Dispatchers.IO) {
            _services.value = coachUseCase.getAllServices().getOrNull() ?: emptyList()
        }
        Log.d("CoachViewmodel", "Setting linked coach: ${_services.value}")
        _buttonState.value = _buttonState.value.copy(
            assignTOService = coach
        )
    }

    fun unsetLinkedCoach() {
        _buttonState.value = _buttonState.value.copy(
            assignTOService = null
        )
        _service.value = null
        _services.value = emptyList()
    }

    fun assignCoachToService(coach: Coach, service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = service.id ?: return@launch
            coachUseCase.assignCoachToService(coach.id, id).onSuccess {
                unsetLinkedCoach()
                Log.d("CoachViewmodel", "Assigning coach to service: $it")
                snackbar.value = "Coach assigned to service successfully"
                initCoaches()
            }.onFailure {
                val message =
                    if (it.localizedMessage?.contains("Coach is already assigned") == true) {
                        "This coach is already assigned to the selected service."
                    } else {
                        "Failed to assign coach to service. Please try again."
                    }
                snackbar.value = message
                unsetLinkedCoach()
            }

        }

    }


    val filteredCoaches = combine(_coaches, searchingCoaches) { coaches, query ->
        if (query.isBlank()) {
            coaches
        } else {
            coaches.filter {
                it.firstName.contains(query, ignoreCase = true) ||
                        it.lastName.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun findCoaches(query: String) {
        searchingCoaches.value = query
    }


}