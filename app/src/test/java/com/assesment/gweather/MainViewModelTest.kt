package com.assesment.gweather

import com.assesment.gweather.data.repository.auth.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainActivityViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true) // relaxed allows void functions like logout()
        viewModel = MainActivityViewModel(authRepository)
    }

    @Test
    fun `isLoggedIn returns true when repository says logged in`() {
        every { authRepository.isLoggedIn() } returns true

        assertTrue(viewModel.isLoggedIn)
    }

    @Test
    fun `isLoggedIn returns false when repository says not logged in`() {
        every { authRepository.isLoggedIn() } returns false

        assertFalse(viewModel.isLoggedIn)
    }

    @Test
    fun `logout delegates to repository`() {
        viewModel.logout()

        verify { authRepository.logout() }
    }
}