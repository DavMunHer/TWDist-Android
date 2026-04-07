package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import com.example.twdist_android.features.projectdetails.domain.model.ProjectNameError
import com.example.twdist_android.features.projectdetails.domain.model.ProjectNameException

fun Throwable.toProjectValidationMessage(): String {
    val projectNameError = (this as? ProjectNameException)?.error ?: return message ?: "Invalid project name"
    return when (projectNameError) {
        ProjectNameError.TooShort -> "Project name must be at least 2 characters"
        ProjectNameError.TooLong -> "Project name must be at most 50 characters"
    }
}

fun Throwable.toProjectActionMessage(fallbackMessage: String): String {
    return if (this is ProjectNameException) {
        toProjectValidationMessage()
    } else {
        fallbackMessage
    }
}
