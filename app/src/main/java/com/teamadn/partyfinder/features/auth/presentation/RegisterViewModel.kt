package com.teamadn.partyfinder.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamadn.partyfinder.features.auth.domain.model.AuthResult
import com.teamadn.partyfinder.features.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterFieldsState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)


class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _fieldsState = MutableStateFlow(RegisterFieldsState())
    val fieldsState = _fieldsState.asStateFlow()

    private val _authState = MutableStateFlow<AuthUIState>(AuthUIState.Idle)
    val authState = _authState.asStateFlow()


    fun onEmailChanged(email: String) {
        _fieldsState.value = _fieldsState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _fieldsState.value = _fieldsState.value.copy(password = password)
    }

    fun onRepeatPasswordChanged(password: String) {
        _fieldsState.value = _fieldsState.value.copy(repeatPassword = password)
    }

    fun onRegisterClicked() {
        viewModelScope.launch {
            _authState.value = AuthUIState.Loading
            val (email, password, repeatPassword) = _fieldsState.value

            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthUIState.Error("Email y contraseña no pueden estar vacíos")
                return@launch
            }
            if (password != repeatPassword) {
                _authState.value = AuthUIState.Error("Las contraseñas no coinciden")
                return@launch
            }
            if (password.length < 6) {
                _authState.value = AuthUIState.Error("La contraseña debe tener al menos 6 caracteres")
                return@launch
            }

            when (val result = registerUseCase(email, password)) {
                is AuthResult.Success -> _authState.value = AuthUIState.Success(result.uid)
                is AuthResult.Error -> _authState.value = AuthUIState.Error(result.message)
            }
        }
    }
}