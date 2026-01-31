package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.presentation.model.RegisterFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.model.shared.Username
import com.example.twdist_android.features.auth.domain.usecases.RegisterUseCase
import com.example.twdist_android.features.auth.presentation.mapper.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterFormState())

    val uiState: StateFlow<RegisterFormState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = null, errorMessage = null) }
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(username = newUsername, usernameError = null, errorMessage = null) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, passwordError = null, errorMessage = null) }
    }

    fun onSubmit() {
        val state = uiState.value

        val emailResult = Email.create(state.email)
        val usernameResult = Username.create(state.username)
        val passwordResult = Password.create(state.password)

        val emailError = emailResult.toUiError()
        val usernameError = usernameResult.toUiError()
        val passwordError = passwordResult.toUiError()

        if (emailError != null || usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    usernameError = usernameError,
                    passwordError = passwordError
                )
            }
            return
        }

        val credentials = RegisterCredentials(
            email = emailResult.getOrThrow(),
            username = usernameResult.getOrThrow(),
            password = passwordResult.getOrThrow()
        )

        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true, 
                    errorMessage = null,
                    emailError = null,
                    usernameError = null,
                    passwordError = null
                ) 
            }
            try {
                registerUseCase(credentials)
                _uiState.update { it.copy(isSuccess = true) }
            } catch (e: IOException) {
                _uiState.update { it.copy(errorMessage = "Server is down or unreachable. Please check your connection.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "An unexpected error occurred: ${e.localizedMessage}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    fun onNavigationHandled() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
