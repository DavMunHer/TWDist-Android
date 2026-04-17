package com.example.twdist_android.features.projectdetails.domain.model

sealed class TaskNameError {
    data object TooShort : TaskNameError()
    data object TooLong : TaskNameError()
}

@JvmInline
value class TaskName private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 50

        fun create(raw: String): Result<TaskName> {
            val trimmed = raw.trim()
            return when {
                trimmed.length < MIN_LENGTH ->
                    Result.failure(TaskNameException(TaskNameError.TooShort))
                trimmed.length > MAX_LENGTH ->
                    Result.failure(TaskNameException(TaskNameError.TooLong))
                else ->
                    Result.success(TaskName(trimmed))
            }
        }
    }

    fun asString(): String = value
}

class TaskNameException(val error: TaskNameError) : IllegalArgumentException()
