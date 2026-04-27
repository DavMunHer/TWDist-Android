package com.example.twdist_android.features.taskdetails.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DISPLAY_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var showCalendar by remember { mutableStateOf(false) }
    val digits = normalizeDateForInput(value).filter(Char::isDigit).take(8)
    val formattedDigits = formatDateDigits(digits)

    OutlinedTextField(
        value = formattedDigits,
        onValueChange = { input ->
            val clean = input.filter(Char::isDigit).take(8)
            onValueChange(formatDateDigits(clean))
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text("DD/MM/YYYY") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = {
            IconButton(onClick = { showCalendar = true }) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Open $label calendar"
                )
            }
        }
    )

    if (showCalendar) {
        val englishContext = remember(context, configuration) {
            val config = Configuration(configuration).apply {
                setLocale(Locale.ENGLISH)
            }
            context.createConfigurationContext(config)
        }
        val initialDate = parseDisplayDate(formattedDigits)
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showCalendar = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val selectedDate = Instant.ofEpochMilli(selectedMillis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        onValueChange(selectedDate.format(DISPLAY_DATE_FORMATTER))
                    }
                    showCalendar = false
                }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalendar = false }) {
                    Text("Cancel")
                }
            }
        ) {
            CompositionLocalProvider(LocalContext provides englishContext) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
}

private fun formatDateDigits(digits: String): String {
    val clean = digits.filter(Char::isDigit).take(8)
    return buildString {
        clean.forEachIndexed { index, c ->
            append(c)
            if ((index == 1 || index == 3) && index != clean.lastIndex) {
                append('/')
            }
        }
    }
}

private fun parseDisplayDate(value: String): LocalDate? {
    if (value.isBlank()) return null
    return parseAnyDateToLocalDate(value)
}

private fun normalizeDateForInput(value: String): String {
    val raw = value.trim()
    if (raw.isBlank()) return ""
    val parsed = parseAnyDateToLocalDate(raw) ?: return raw
    return parsed.format(DISPLAY_DATE_FORMATTER)
}

private fun parseAnyDateToLocalDate(raw: String): LocalDate? {
    return runCatching { LocalDate.parse(raw, DISPLAY_DATE_FORMATTER) }.getOrNull()
        ?: runCatching { LocalDate.parse(raw) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(raw).toLocalDate() }.getOrNull()
        ?: runCatching { Instant.parse(raw).atZone(ZoneId.systemDefault()).toLocalDate() }.getOrNull()
}
