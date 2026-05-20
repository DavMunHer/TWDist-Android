package com.example.twdist_android.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.twdist_android.core.ui.navigation.NavigationRoot
import com.example.twdist_android.features.auth.domain.model.SessionStatus
import com.example.twdist_android.features.auth.presentation.viewmodel.SessionViewModel

@Composable
fun AuthGate(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionStatus by sessionViewModel.sessionStatus.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavigationRoot(
            sessionStatus = sessionStatus,
            onLogout = { sessionViewModel.logout() }
        )

        if (sessionStatus is SessionStatus.Checking) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
