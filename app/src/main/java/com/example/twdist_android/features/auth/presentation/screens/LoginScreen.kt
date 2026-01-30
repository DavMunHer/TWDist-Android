package com.example.twdist_android.features.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.twdist_android.features.auth.presentation.model.LoginFormState
import com.example.twdist_android.features.auth.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LoginContent(
        uiState = state,
        onEmailChange = { viewModel.updateEmail(it) },
        onPasswordChange = { viewModel.updatePassword(it) },
        onSubmit = { viewModel.onSubmit() },
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    uiState: LoginFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = uiState.email,
            label = { Text("Email") },
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = uiState.password,
            label = { Text("Password") },
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = onSubmit) {
            Text("Send")
        }
        // TODO: Display error messages in the screen
    }
}

@Preview
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
        onSubmit = {}
    )
}