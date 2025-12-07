package com.teamadn.partyfinder.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamadn.partyfinder.features.auth.domain.model.AuthResult
import com.teamadn.partyfinder.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginFieldsState(
    val email: String = "",
    val password: String = ""
)

sealed class AuthUIState {
    object Idle : AuthUIState()
    object Loading : AuthUIState()
    data class Success(val uid: String) : AuthUIState()
    data class Error(val message: String) : AuthUIState()
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _fieldsState = MutableStateFlow(LoginFieldsState())
    val fieldsState = _fieldsState.asStateFlow()

    private val _authState = MutableStateFlow<AuthUIState>(AuthUIState.Idle)
    val authState = _authState.asStateFlow()

    fun onEmailChanged(email: String) {
        _fieldsState.value = _fieldsState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _fieldsState.value = _fieldsState.value.copy(password = password)
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _authState.value = AuthUIState.Loading
            val (email, password) = _fieldsState.value

            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthUIState.Error("Email y contraseña no pueden estar vacíos")
                return@launch
            }

            when (val result = loginUseCase(email, password)) {
                is AuthResult.Success -> _authState.value = AuthUIState.Success(result.uid)
                is AuthResult.Error -> _authState.value = AuthUIState.Error(result.message)
            }
        }
    }
}