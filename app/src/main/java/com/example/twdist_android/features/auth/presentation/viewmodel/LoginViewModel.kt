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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginFormState())

    val uiState: StateFlow<LoginFormState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onSubmit() {
        val state = _uiState.value

        val emailResult = Email.create(state.email)
        val passwordResult = Password.create(state.password)

        if (emailResult.isFailure || passwordResult.isFailure) {
            _uiState.update {
                it.copy(
                    emailError = emailResult.toUiError(),
                    passwordError = passwordResult.toUiError()
                )
            }
            return
        }

        val credentials = LoginCredentials(
            email = emailResult.getOrThrow(),
            password = passwordResult.getOrThrow()
        )

        viewModelScope.launch {
            loginUseCase(credentials)
        }
    }

}