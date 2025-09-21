package com.assesment.gweather.ui.auth.register

import com.assesment.gweather.data.repository.auth.AuthRepository
import com.assesment.gweather.rule.MainDispatcherRule
import com.assesment.gweather.ui.model.AuthUiState
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: RegisterViewModel
    private val repository: AuthRepository = mockk()

    @Before
    fun setup() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `register success updates state to Success`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        coEvery { repository.register("new@test.com", "1234") } returns Result.success(mockUser)

        viewModel.register("new@test.com", "1234")

        assertTrue(viewModel.registerState.value is AuthUiState.Success)
    }

    @Test
    fun `register failure updates state to Error`() = runTest {
        coEvery {
            repository.register(
                "bad@test.com",
                "weak"
            )
        } returns Result.failure(Exception("Registration failed"))

        viewModel.register("bad@test.com", "weak")

        assertTrue(viewModel.registerState.value is AuthUiState.Error)
    }
}
