package com.teamadn.partyfinder.features.auth.domain.usecase

import com.teamadn.partyfinder.features.auth.domain.repository.IAuthRepository

class LoginUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}