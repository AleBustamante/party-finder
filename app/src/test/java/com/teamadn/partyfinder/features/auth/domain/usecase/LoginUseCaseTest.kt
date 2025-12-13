package com.teamadn.partyfinder.features.auth.domain.usecase

import com.teamadn.partyfinder.features.auth.domain.model.AuthResult
import com.teamadn.partyfinder.features.auth.domain.repository.IAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUseCaseTest {

    // 1. Mockeamos el repositorio (simulamos su comportamiento)
    private val repository: IAuthRepository = mockk()

    // 2. Instanciamos la clase que vamos a probar
    private val loginUseCase = LoginUseCase(repository)

    @Test
    fun `invoke should call repository login and return success`() = runBlocking {
        // GIVEN (Dado)
        val email = "test@example.com"
        val password = "password123"
        val expectedResult = AuthResult.Success("uid_123")

        // Configuramos el mock: cuando llamen a login, devuelve Success
        coEvery { repository.login(email, password) } returns expectedResult

        // WHEN (Cuando)
        val result = loginUseCase(email, password)

        // THEN (Entonces)
        assertEquals(expectedResult, result)
        // Verificamos que el repositorio fue llamado exactamente una vez con esos datos
        coVerify(exactly = 1) { repository.login(email, password) }
    }

    @Test
    fun `invoke should return error when repository fails`() = runBlocking {
        // GIVEN
        val email = "test@example.com"
        val password = "wrong"
        val expectedResult = AuthResult.Error("Credenciales inválidas")

        coEvery { repository.login(email, password) } returns expectedResult

        // WHEN
        val result = loginUseCase(email, password)

        // THEN
        assertEquals(expectedResult, result)
    }
}