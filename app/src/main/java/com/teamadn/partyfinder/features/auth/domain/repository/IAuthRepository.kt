package com.teamadn.partyfinder.features.auth.domain.repository

import com.teamadn.partyfinder.features.auth.domain.model.AuthResult

interface IAuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String): AuthResult
    fun logout()
}