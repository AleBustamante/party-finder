package com.teamadn.partyfinder.features.auth.domain.usecase

import com.teamadn.partyfinder.features.auth.domain.repository.IAuthRepository

class RegisterUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.register(email, password)
}