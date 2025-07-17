package com.fola.scss.main.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fola.domain.model.QA
import com.fola.domain.model._Appointment
import com.fola.domain.usecase.CoachUseCase
import com.fola.domain.usecase.HomeUseCase
import com.fola.scss.main.coach.CoachViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Stat(val title: String, val count: Int, val icon: ImageVector, val color: Color)

data class States(
    val appointments: Stat = Stat("Appointments", 0, Icons.Filled.DateRange, Color(0xFF6200EE)),
    val services: Stat = Stat("Services", 0, Icons.Filled.DateRange, Color(0xFF03DAC5)),
    val coaches: Stat = Stat("Coaches", 0, Icons.Filled.DateRange, Color(0xFFFF5722)),
    val members: Stat = Stat("Members", 0, Icons.Filled.DateRange, Color(0xFF4CAF50))
)


class DashboardViewModel(
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _stats = MutableStateFlow(States())
    val states = _stats.asStateFlow()

    private val _appointments = MutableStateFlow<List<_Appointment>>(emptyList())
    val appointments = _appointments.asStateFlow()

    private val _qas = MutableStateFlow<List<QA>>(emptyList())
    val unansweredQAs = _qas.asStateFlow()
    private val _qa = MutableStateFlow<QA?>(null)
    val qa = _qa.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _stats.value = _stats.value.copy(
                appointments = _stats.value.appointments.copy(
                    count = useCase.getAllAppointments().getOrNull()?.size ?: 0
                ),
                services = _stats.value.services.copy(
                    count = useCase.getCoachesNumber().getOrNull() ?: 0
                ),
                coaches = _stats.value.coaches.copy(
                    count = useCase.getCoachesNumber().getOrNull() ?: 0
                )
            )
            getAllAppointments()
            getAllQAs()
        }

    }

    fun getAllAppointments() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCase.getAllAppointments()
            if (result.isSuccess) {
                _appointments.value = result.getOrNull() ?: emptyList()
            } else {
                _appointments.value = emptyList()
            }
        }
    }

    fun getAllQAs() {
        viewModelScope.launch(Dispatchers.IO) {
            _qas.value = useCase.getUnansweredQA()
        }
    }


    fun answerQA(qa: QA) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DashboardViewModel", "Answering QA: ${qa.question}")
            val result = useCase.answerQA(qa)
            if (result.isSuccess) {
                Log.d("DashboardViewModel", "QA answered successfully: ${qa.question}")
                getAllQAs()
                setSnackbarMessage("QA answered successfully")
                clearAnsweredQA()
            } else {
                Log.e("DashboardViewModel", "Failed to answer QA: ${result.exceptionOrNull()?.message}")
                setSnackbarMessage(result.exceptionOrNull()?.message ?: "Failed to answer QA")
            }
        }
    }


    fun setSnackbarMessage(message: String?) {
        _snackbarMessage.value = message
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }


    fun setAnsweredQA(qa: QA) {
        _qa.value = qa
    }

    fun clearAnsweredQA() {
        _qa.value = null
    }

    fun setQAAnswer(it: String) {
        _qa.value = _qa.value?.copy(answer = it)
    }
}


class DashBoardModelFactory(
    private val homeUseCase: HomeUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(homeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



