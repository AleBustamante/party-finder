package com.teamadn.partyfinder.features.auth.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.teamadn.partyfinder.features.auth.domain.model.AuthResult
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource(private val firebaseAuth: FirebaseAuth) {

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw IllegalStateException("Firebase UID nulo")
            AuthResult.Success(uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun register(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw IllegalStateException("Firebase UID nulo")
            AuthResult.Success(uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido")
        }
    }
}