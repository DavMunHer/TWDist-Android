package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import com.example.twdist_android.features.projectdetails.domain.model.ProjectNameError
import com.example.twdist_android.features.projectdetails.domain.model.ProjectNameException
import com.example.twdist_android.features.projectdetails.domain.model.SectionNameError
import com.example.twdist_android.features.projectdetails.domain.model.SectionNameException
import com.example.twdist_android.features.projectdetails.domain.model.TaskNameError
import com.example.twdist_android.features.projectdetails.domain.model.TaskNameException

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

fun Throwable.toValidationMessage(): String {
    val sectionNameError = (this as? SectionNameException)?.error ?: return message ?: "Invalid section name"
    return when (sectionNameError) {
        SectionNameError.TooShort -> "Section name must be at least 2 characters"
        SectionNameError.TooLong -> "Section name must be at most 50 characters"
    }
}

fun Throwable.toTaskValidationMessage(): String {
    val taskNameError = (this as? TaskNameException)?.error ?: return message ?: "Invalid task name"
    return when (taskNameError) {
        TaskNameError.TooShort -> "Task name must be at least 2 characters"
        TaskNameError.TooLong -> "Task name must be at most 50 characters"
    }
}
