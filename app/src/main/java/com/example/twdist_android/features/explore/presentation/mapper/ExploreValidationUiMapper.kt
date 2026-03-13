package com.example.twdist_android.features.explore.presentation.mapper

import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectNameError
import com.example.twdist_android.features.explore.domain.model.ProjectNameException

fun Result<ProjectName>.toUiError(): String? =
    exceptionOrNull()
        ?.let { it as? ProjectNameException }
        ?.error
        ?.let {
            when (it) {
                ProjectNameError.TooShort ->
                    "Project name must be at least 2 characters long"
                ProjectNameError.TooLong ->
                    "Project name must be no more than 50 characters long"
            }
        }
