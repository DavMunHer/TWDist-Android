package com.example.twdist_android.features.auth.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun TermsAndPrivacyText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val text = buildAnnotatedString {
        append("By continuing, you agree to our ")

        pushLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                linkInteractionListener = LinkInteractionListener {
                    onTermsClick()
                }
            )
        )
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Terms of Service")
        }
        pop()

        append(" and ")

        pushLink(
            LinkAnnotation.Clickable(
                tag = "privacy",
                linkInteractionListener = LinkInteractionListener {
                    onPrivacyClick()
                }
            )
        )
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Privacy Policy")
        }
        pop()

        append(".")
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall
    )
}
