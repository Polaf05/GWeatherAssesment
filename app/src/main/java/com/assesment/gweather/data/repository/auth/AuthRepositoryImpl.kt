package com.assesment.gweather.data.repository.auth

import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<FirebaseUser> =
        remoteDataSource.login(email, password)


    override suspend fun register(email: String, password: String): Result<FirebaseUser> =
        remoteDataSource.register(email, password)


    override fun logout() = remoteDataSource.logout()


    override fun currentUser(): FirebaseUser? = remoteDataSource.currentUser()


    override fun isLoggedIn(): Boolean = remoteDataSource.isLoggedIn()
}