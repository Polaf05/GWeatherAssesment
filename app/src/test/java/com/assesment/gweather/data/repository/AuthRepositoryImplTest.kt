package com.assesment.gweather.data.repository

import com.assesment.gweather.data.repository.auth.AuthRemoteDataSource
import com.assesment.gweather.data.repository.auth.AuthRepository
import com.assesment.gweather.data.repository.auth.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val remoteDataSource: AuthRemoteDataSource = mockk()
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(remoteDataSource)
    }

    // 🔹 LOGIN TESTS
    @Test
    fun `login returns success`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        coEvery { remoteDataSource.login("test@mail.com", "123456") } returns Result.success(mockUser)

        val result = repository.login("test@mail.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
    }

    @Test
    fun `login returns failure`() = runTest {
        coEvery { remoteDataSource.login(any(), any()) } returns Result.failure(Exception("Invalid"))

        val result = repository.login("wrong@mail.com", "badpass")

        assertTrue(result.isFailure)
        assertEquals("Invalid", result.exceptionOrNull()?.message)
    }

    // 🔹 REGISTER TESTS
    @Test
    fun `register returns success`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        coEvery { remoteDataSource.register("new@mail.com", "123456") } returns Result.success(mockUser)

        val result = repository.register("new@mail.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
    }

    @Test
    fun `register returns failure`() = runTest {
        coEvery { remoteDataSource.register(any(), any()) } returns Result.failure(Exception("Weak password"))

        val result = repository.register("fail@mail.com", "bad")

        assertTrue(result.isFailure)
        assertEquals("Weak password", result.exceptionOrNull()?.message)
    }
}