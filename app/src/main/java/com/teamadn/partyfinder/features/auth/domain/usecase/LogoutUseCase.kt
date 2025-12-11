package com.teamadn.partyfinder.features.auth.domain.usecase

import com.teamadn.partyfinder.features.auth.domain.repository.IAuthRepository

class LogoutUseCase(private val repository: IAuthRepository) {
    operator fun invoke() = repository.logout()
}