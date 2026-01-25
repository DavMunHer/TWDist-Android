package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.presentation.model.RegisterFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.twdist_android.features.auth.domain.model.User
import com.example.twdist_android.features.auth.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterFormState())

    val uiState: StateFlow<RegisterFormState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onSubmit() {
        val current = uiState.value
        val req = RegisterRequestDto(
            email = current.email,
            username = current.username,
            password = current.password
        )

        viewModelScope.launch {
            try {
                val user: User = registerUseCase(req)
                // TODO: Handle successful registration (e.g., navigate to another screen)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(password = "Error: ${e.message}")
                }
            }
        }
    }
}