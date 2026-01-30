package com.example.twdist_android.features.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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
    Column {
        TextField(
            value = uiState.email,
            label = { Text("Email") },
            onValueChange = onEmailChange
        )
        TextField(
            value = uiState.username,
            label = { Text("Username") },
            onValueChange = onUsernameChange
        )
        TextField(
            value = uiState.password,
            label = { Text("Password") },
            onValueChange = onPasswordChange
        )
        Button(onClick = { onFormSend() }) {
            Text("Register")
        }
        // TODO: Display error messages in the screen
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