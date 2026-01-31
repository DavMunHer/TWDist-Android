package com.example.twdist_android.features.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.twdist_android.features.auth.presentation.components.AuthTextField
import com.example.twdist_android.features.auth.presentation.components.GoToRegisterText
import com.example.twdist_android.features.auth.presentation.components.PasswordTextField
import com.example.twdist_android.features.auth.presentation.components.TermsAndPrivacyText
import com.example.twdist_android.features.auth.presentation.model.LoginFormState
import com.example.twdist_android.features.auth.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToExplore()
            viewModel.onNavigationHandled()
        }
    }

    LoginContent(
        uiState = state,
        onEmailChange = { viewModel.updateEmail(it) },
        onPasswordChange = { viewModel.updatePassword(it) },
        onSubmit = { viewModel.onSubmit() },
        modifier = modifier,
        onTermsClick = onTermsClick,
        onPrivacyClick = onPrivacyClick,
        onRegisterClick = onNavigateToRegister
    )
}

@Composable
fun LoginContent(
    uiState: LoginFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {

        Text(
            text = "Welcome back!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.size(15.dp))

        Text("Fill the form to log in to your account.")

        Spacer(Modifier.size(15.dp))

        AuthTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            error = uiState.emailError,
        )

        Spacer(Modifier.size(10.dp))

        PasswordTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            modifier = Modifier.fillMaxWidth(),
            error = uiState.passwordError
        )

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.size(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }

        Spacer(Modifier.size(10.dp))

        TermsAndPrivacyText(
            onTermsClick = onTermsClick,
            onPrivacyClick = onPrivacyClick
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        GoToRegisterText(
            onRegisterClick = onRegisterClick
        )

    }
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
    var state by remember {
        mutableStateOf(LoginFormState())
    }
    LoginContent(
        uiState = state,
        onEmailChange = { newEmail ->
            state = state.copy(email = newEmail)
        },
        onPasswordChange = { newPassword ->
            state = state.copy(password = newPassword)
        },
        onSubmit = {},
        onTermsClick = {},
        onPrivacyClick = {},
        onRegisterClick = {}
    )
}
