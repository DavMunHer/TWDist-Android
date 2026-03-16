package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.model.shared.Username
import com.example.twdist_android.features.auth.domain.usecases.RegisterUseCase
import com.example.twdist_android.features.auth.presentation.mapper.toCredentials
import com.example.twdist_android.features.auth.presentation.mapper.toUiError
import com.example.twdist_android.features.auth.presentation.model.RegisterFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

        // Validate all fields at once via the presentation mapper
        val emailError = Email.create(state.email).toUiError()
        val usernameError = Username.create(state.username).toUiError()
        val passwordError = Password.create(state.password).toUiError()

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

        // Map form state → domain credentials (safe after validation above)
        val credentialsResult = state.toCredentials()
        if (credentialsResult.isFailure) return

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

            registerUseCase(credentialsResult.getOrThrow())
                .onSuccess {
                    _uiState.update { it.copy(isSuccess = true) }
                }
                .onFailure { error ->
                    val message = when (error) {
                        is IOException -> "Server is down or unreachable. Please check your connection."
                        else -> error.localizedMessage ?: "An unexpected error occurred."
                    }
                    _uiState.update { it.copy(errorMessage = message) }
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}



