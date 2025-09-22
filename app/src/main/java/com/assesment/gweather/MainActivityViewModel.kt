package com.assesment.gweather

import androidx.lifecycle.ViewModel
import com.assesment.gweather.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isLoggedIn: Boolean
        get() = authRepository.isLoggedIn()

    fun logout() = authRepository.logout()
}