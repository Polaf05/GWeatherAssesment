package com.assesment.gweather.ui.auth.login


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
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private val repository: AuthRepository = mockk()

    @Before
    fun setup() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `login success updates state to Success`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        coEvery { repository.login("test@test.com", "1234") } returns Result.success(mockUser)

        viewModel.login("test@test.com", "1234")

        assertTrue(viewModel.loginState.value is AuthUiState.Success)
    }

    @Test
    fun `login failure updates state to Error`() = runTest {
        coEvery { repository.login("wrong@test.com", "bad") } returns Result.failure(Exception("Invalid"))

        viewModel.login("wrong@test.com", "bad")

        assertTrue(viewModel.loginState.value is AuthUiState.Error)
    }
}