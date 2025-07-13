package com.fola.scss.auth.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fola.domain.repo.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _snackbarMessage = MutableStateFlow("")
    val snackbarMessage = _snackbarMessage.asStateFlow()


    private var _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private var _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private var _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    val loadingIcon = MutableStateFlow(false)

    fun logIn() {
        loadingIcon.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val  result = authRepository.login(
                userName = _email.value,
                password = _password.value
            )
           if (result.isFailure){
               _snackbarMessage.value = result.exceptionOrNull()?.localizedMessage ?: "can't login right now"
           }
        }
    }

    fun setEmail(email: String) {
        _email.value = email
        _snackbarMessage.value = ""
    }

    fun setPassword(p: String) {
        _password.value = p
        _snackbarMessage.value = ""
    }

    fun setVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun stopLoadingIcon() {
        _snackbarMessage.value = ""
        loadingIcon.value = false
    }

    fun setSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }
}
