package com.example.twdist_android.features.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.twdist_android.features.auth.presentation.components.PasswordTextField
import com.example.twdist_android.features.auth.presentation.model.RegisterFormState
import com.example.twdist_android.features.auth.presentation.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    RegisterContent(
        uiState = state,
        onEmailChange = { viewModel.updateEmail(it) },
        onUsernameChange = { viewModel.updateUsername(it) },
        onPasswordChange = { viewModel.updatePassword(it) },
        onFormSend = { viewModel.onSubmit() },
        modifier = modifier
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterFormState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onFormSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Register",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.size(15.dp))

        Text("Fill the form with your information")

        Spacer(Modifier.size(15.dp))


        AuthTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            modifier = Modifier.fillMaxWidth(),
            error = uiState.emailError
        )

        Spacer(Modifier.size(10.dp))

        AuthTextField(
            value = uiState.username,
            onValueChange = onUsernameChange,
            label = "Username",
            modifier = Modifier.fillMaxWidth(),
            error = uiState.usernameError
        )
        Spacer(Modifier.size(10.dp))

        PasswordTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            modifier = Modifier.fillMaxWidth(),
            error = uiState.passwordError
        )
        Spacer(Modifier.size(20.dp))

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = { onFormSend() },
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
                Text("Register")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    var state by remember {
        mutableStateOf(RegisterFormState())
    }
    RegisterContent(
        uiState = state,
        onEmailChange = { newEmail ->
            state = state.copy(email = newEmail)
        },
        onUsernameChange = { newUsername ->
            state = state.copy(username = newUsername)
        },
        onPasswordChange = { newPassword ->
            state = state.copy(password = newPassword)
        },
        onFormSend = {}
    )
}
