package com.example.twdist_android.features.auth.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun GoToRegisterText(
    onRegisterClick: () -> Unit
) {
    val text = buildAnnotatedString {
        append("Don’t have an account? ")

        pushLink(
            LinkAnnotation.Clickable(
                tag = "register",
                linkInteractionListener = LinkInteractionListener {
                    onRegisterClick()
                }
            )
        )
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Sign up")
        }
        pop()
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}
