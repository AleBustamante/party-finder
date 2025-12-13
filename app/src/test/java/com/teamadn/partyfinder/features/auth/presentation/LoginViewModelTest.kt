package com.teamadn.partyfinder.features.auth.presentation

import com.teamadn.partyfinder.MainDispatcherRule
import com.teamadn.partyfinder.features.auth.domain.model.AuthResult
import com.teamadn.partyfinder.features.auth.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Aplicamos la regla para viewModelScope
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `initial state should be Idle and fields empty`() {
        viewModel = LoginViewModel(loginUseCase)

        assertTrue(viewModel.authState.value is AuthUIState.Idle)
        assertEquals("", viewModel.fieldsState.value.email)
        assertEquals("", viewModel.fieldsState.value.password)
    }

    @Test
    fun `onEmailChanged and onPasswordChanged should update fields state`() {
        viewModel = LoginViewModel(loginUseCase)

        viewModel.onEmailChanged("user@test.com")
        viewModel.onPasswordChanged("123456")

        assertEquals("user@test.com", viewModel.fieldsState.value.email)
        assertEquals("123456", viewModel.fieldsState.value.password)
    }

    @Test
    fun `onLoginClicked with empty fields should set Error state and NOT call usecase`() = runTest {
        viewModel = LoginViewModel(loginUseCase)

        // No seteamos email ni password (están vacíos por defecto)
        viewModel.onLoginClicked()

        // Verificamos que sea Error
        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthUIState.Error)
        assertEquals("Email y contraseña no pueden estar vacíos", (currentState as AuthUIState.Error).message)

        // Verificamos que NUNCA se llamó al caso de uso
        coVerify(exactly = 0) { loginUseCase(any(), any()) }
    }

    @Test
    fun `onLoginClicked with valid fields and success result should set Success state`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        val email = "valid@test.com"
        val password = "secure"
        val uid = "user_123"

        // Simulamos comportamiento exitoso del UseCase
        coEvery { loginUseCase(email, password) } returns AuthResult.Success(uid)

        // Simulamos entrada de usuario
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        // Acción
        viewModel.onLoginClicked()

        // Verificación
        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthUIState.Success)
        assertEquals(uid, (currentState as AuthUIState.Success).uid)
    }

    @Test
    fun `onLoginClicked with valid fields but error result should set Error state`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        val email = "valid@test.com"
        val password = "wrong"
        val errorMessage = "Login fallido"

        // Simulamos error del UseCase
        coEvery { loginUseCase(email, password) } returns AuthResult.Error(errorMessage)

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.onLoginClicked()

        val currentState = viewModel.authState.value
        assertTrue(currentState is AuthUIState.Error)
        assertEquals(errorMessage, (currentState as AuthUIState.Error).message)
    }
}