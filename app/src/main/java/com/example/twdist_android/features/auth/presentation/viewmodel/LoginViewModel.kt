package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.usecases.LoginUseCase
import com.example.twdist_android.features.auth.presentation.mapper.toUiError
import com.example.twdist_android.features.auth.presentation.model.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginFormState())

    val uiState: StateFlow<LoginFormState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = null, errorMessage = null) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update {
            it.copy(
                password = newPassword,
                passwordError = null,
                errorMessage = null
            )
        }
    }

    fun onSubmit() {
        val state = _uiState.value

        val emailResult = Email.create(state.email)
        val passwordResult = Password.create(state.password)

        val emailError = emailResult.toUiError()
        val passwordError = passwordResult.toUiError()

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        val credentials = LoginCredentials(
            email = emailResult.getOrThrow(),
            password = passwordResult.getOrThrow()
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    emailError = null,
                    passwordError = null
                )
            }
            try {
                loginUseCase(credentials)
                // Handle success (e.g., navigation)
            } catch (e: IOException) {
                _uiState.update { it.copy(errorMessage = "Server is down or unreachable. Please check your connection.") }
            } catch (e: Exception) {
                val message = e.localizedMessage
                if (message!!.contains("403")) {
                    _uiState.update { it.copy(errorMessage = "Invalid credentials. Please try again.") }
                } else {
                    _uiState.update { it.copy(errorMessage = "An unexpected error occurred: ${e.localizedMessage}") }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
