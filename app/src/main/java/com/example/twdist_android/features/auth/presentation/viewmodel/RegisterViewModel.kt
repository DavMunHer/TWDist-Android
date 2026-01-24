package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.twdist_android.features.auth.presentation.model.RegisterFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterFormState())

    val uiState: StateFlow<RegisterFormState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(email = newUsername) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(email = newPassword) }
    }

    fun onSubmit() {
        //TODO: here goes the createUser method form the repository
    }
}