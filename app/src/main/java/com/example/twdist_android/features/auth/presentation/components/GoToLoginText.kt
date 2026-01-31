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
fun GoToLoginText(
    onLoginClick: () -> Unit
) {
    val text = buildAnnotatedString {
        append("Already have an account? ")

        pushLink(
            LinkAnnotation.Clickable(
                tag = "login",
                linkInteractionListener = LinkInteractionListener {
                    onLoginClick()
                }
            )
        )
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Go to login")
        }
        pop()
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}
