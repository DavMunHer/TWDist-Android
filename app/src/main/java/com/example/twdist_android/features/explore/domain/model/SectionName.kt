package com.example.twdist_android.features.explore.domain.model

sealed class SectionNameError {
	data object TooShort : SectionNameError()
	data object TooLong : SectionNameError()
}

@JvmInline
value class SectionName private constructor(val value: String) {
	companion object {
		private const val MIN_LENGTH = 2
		private const val MAX_LENGTH = 50

		fun create(raw: String): Result<SectionName> {
			val trimmed = raw.trim()
			return when {
				trimmed.length < MIN_LENGTH ->
					Result.failure(SectionNameException(SectionNameError.TooShort))
				trimmed.length > MAX_LENGTH ->
					Result.failure(SectionNameException(SectionNameError.TooLong))
				else ->
					Result.success(SectionName(trimmed))
			}
		}
	}

	fun asString(): String = value
}

class SectionNameException(val error: SectionNameError) : IllegalArgumentException()

