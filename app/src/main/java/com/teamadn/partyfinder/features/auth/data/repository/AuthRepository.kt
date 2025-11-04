package com.teamadn.partyfinder.features.auth.data.repository

import com.teamadn.partyfinder.features.auth.data.datasource.AuthRemoteDataSource
import com.teamadn.partyfinder.features.auth.domain.model.AuthResult
import com.teamadn.partyfinder.features.auth.domain.repository.IAuthRepository

class AuthRepository(
    private val remoteDataSource: AuthRemoteDataSource
) : IAuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        // En un futuro, aquí podrías añadir lógica para consultar
        // si el usuario tiene datos en la base de datos local (Room)
        return remoteDataSource.login(email, password)
    }

    override suspend fun register(email: String, password: String): AuthResult {
        // Al registrarse, podríamos guardar el usuario en Room
        // o crear una entrada en Realtime Database.
        return remoteDataSource.register(email, password)
    }
}