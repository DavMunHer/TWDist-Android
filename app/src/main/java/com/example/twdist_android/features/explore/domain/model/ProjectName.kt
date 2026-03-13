package com.example.twdist_android.features.explore.domain.model

sealed class ProjectNameError {
    data object TooShort : ProjectNameError()
    data object TooLong : ProjectNameError()
}

@JvmInline
value class ProjectName private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 50

        fun create(raw: String): Result<ProjectName> {
            val trimmed = raw.trim()
            return when {
                trimmed.length < MIN_LENGTH ->
                    Result.failure(ProjectNameException(ProjectNameError.TooShort))
                trimmed.length > MAX_LENGTH ->
                    Result.failure(ProjectNameException(ProjectNameError.TooLong))
                else ->
                    Result.success(ProjectName(trimmed))
            }
        }
    }

    fun asString(): String = value
}

class ProjectNameException(val error: ProjectNameError) : IllegalArgumentException()
