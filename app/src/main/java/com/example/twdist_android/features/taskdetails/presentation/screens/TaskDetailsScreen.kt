package com.example.twdist_android.features.taskdetails.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import android.content.res.Configuration
import com.example.twdist_android.features.projectdetails.presentation.components.TaskCard
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi
import com.example.twdist_android.features.taskdetails.presentation.event.TaskDetailsEvent
import com.example.twdist_android.features.taskdetails.presentation.model.TaskDetailsUiEvent
import com.example.twdist_android.features.taskdetails.presentation.viewmodel.TaskDetailsViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun TaskDetailsScreen(
    projectId: Long,
    sectionId: Long,
    taskId: Long,
    onNavigateBackToProjectDetails: () -> Unit = {},
    viewModel: TaskDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTask = uiState.task

    LaunchedEffect(projectId, sectionId, taskId) {
        viewModel.loadTaskDetails(projectId = projectId, sectionId = sectionId, taskId = taskId)
    }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                TaskDetailsUiEvent.NavigateBackToProjectDetails -> onNavigateBackToProjectDetails()
            }
        }
    }

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(message = uiState.error!!)
        selectedTask != null -> TaskDetailsContent(
            projectName = uiState.projectName,
            sectionId = sectionId,
            task = selectedTask,
            isMenuOpen = uiState.openProjectMenu,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            description = uiState.description,
            isSaving = uiState.isSaving,
            saveMessage = uiState.saveMessage,
            openTaskMenuForId = uiState.openTaskMenuForId,
            editingTaskId = uiState.editingTaskId,
            editingTaskName = uiState.editingTaskName,
            deleteConfirmTaskId = uiState.deleteConfirmTaskId,
            taskActionError = uiState.taskActionError,
            isTaskEditLoading = uiState.isTaskEditLoading,
            isTaskDeleteLoading = uiState.isTaskDeleteLoading,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun TaskDetailsContent(
    projectName: String,
    sectionId: Long,
    task: TaskUi,
    isMenuOpen: Boolean,
    startDate: String,
    endDate: String,
    description: String,
    isSaving: Boolean,
    saveMessage: String?,
    openTaskMenuForId: Long?,
    editingTaskId: Long?,
    editingTaskName: String,
    deleteConfirmTaskId: Long?,
    taskActionError: String?,
    isTaskEditLoading: Boolean,
    isTaskDeleteLoading: Boolean,
    onEvent: (TaskDetailsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = projectName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Box {
                IconButton(onClick = { onEvent(TaskDetailsEvent.MenuOpened) }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Project Options"
                    )
                }
                DropdownMenu(
                    expanded = isMenuOpen,
                    onDismissRequest = { onEvent(TaskDetailsEvent.MenuDismissed) }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { onEvent(TaskDetailsEvent.MenuEditClicked) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { onEvent(TaskDetailsEvent.MenuDeleteClicked) }
                    )
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        TaskCard(
            sectionId = sectionId,
            taskItem = task,
            isTaskMenuOpen = openTaskMenuForId == task.id,
            onTaskEvent = { taskEvent ->
                when (taskEvent) {
                    TaskEvent.TaskMenuDismissed -> onEvent(TaskDetailsEvent.TaskMenuDismissed)
                    is TaskEvent.TaskMenuOpened -> onEvent(TaskDetailsEvent.TaskMenuOpened(taskEvent.taskId))
                    is TaskEvent.TaskCompletionToggled -> onEvent(
                        TaskDetailsEvent.TaskCompletionToggled(
                            sectionId = taskEvent.sectionId,
                            taskId = taskEvent.taskId
                        )
                    )
                    is TaskEvent.EditTaskClicked -> onEvent(
                        TaskDetailsEvent.TaskEditClicked(
                            sectionId = taskEvent.sectionId,
                            taskId = taskEvent.taskId
                        )
                    )
                    is TaskEvent.DeleteTaskClicked -> onEvent(
                        TaskDetailsEvent.TaskDeleteClicked(
                            sectionId = taskEvent.sectionId,
                            taskId = taskEvent.taskId
                        )
                    )
                    else -> Unit
                }
            },
            onTaskClick = null
        )

        OutlinedTextField(
            value = description,
            onValueChange = { onEvent(TaskDetailsEvent.DescriptionChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            label = { Text("Description") }
        )

        DateInputField(
            label = "Start Date",
            value = startDate,
            onValueChange = { onEvent(TaskDetailsEvent.StartDateChanged(it)) }
        )

        DateInputField(
            label = "End Date",
            value = endDate,
            onValueChange = { onEvent(TaskDetailsEvent.EndDateChanged(it)) }
        )

        Button(
            onClick = { onEvent(TaskDetailsEvent.SaveClicked) },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isSaving) "Saving..." else "Save Changes")
        }

        if (!saveMessage.isNullOrBlank()) {
            Text(
                text = saveMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (!taskActionError.isNullOrBlank()) {
            Text(
                text = taskActionError,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {},
            enabled = false
        ) {
            Text(text = "Add Subtask")
        }

        if (editingTaskId != null) {
            AlertDialog(
                onDismissRequest = { onEvent(TaskDetailsEvent.TaskEditDismissed) },
                title = { Text("Edit Task") },
                text = {
                    OutlinedTextField(
                        value = editingTaskName,
                        onValueChange = { onEvent(TaskDetailsEvent.TaskEditNameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Task name") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onEvent(TaskDetailsEvent.TaskEditConfirmed) },
                        enabled = !isTaskEditLoading
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(TaskDetailsEvent.TaskEditDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (deleteConfirmTaskId != null) {
            AlertDialog(
                onDismissRequest = { onEvent(TaskDetailsEvent.TaskDeleteDismissed) },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    Button(
                        onClick = { onEvent(TaskDetailsEvent.TaskDeleteConfirmed) },
                        enabled = !isTaskDeleteLoading
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(TaskDetailsEvent.TaskDeleteDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var showCalendar by remember { mutableStateOf(false) }
    val normalizedValue = normalizeDateForInput(value)
    val digits = normalizedValue.filter(Char::isDigit).take(8)

    OutlinedTextField(
        value = digits,
        onValueChange = { input ->
            val clean = input.filter(Char::isDigit).take(8)
            onValueChange(formatDateDigits(clean))
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text("DD/MM/YYYY") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = DateDigitsVisualTransformation,
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
        val initialDate = parseDisplayDate(normalizedValue)
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
            androidx.compose.runtime.CompositionLocalProvider(LocalContext provides englishContext) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val DISPLAY_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun formatDateDigits(digits: String): String {
    val clean = digits.filter(Char::isDigit).take(8)
    return buildString {
        clean.forEachIndexed { index, c ->
            append(c)
            if (index == 1 || index == 3) append('/')
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

private object DateDigitsVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text.filter(Char::isDigit).take(8)
        val day = input.take(2).padEnd(2, '_')
        val month = input.drop(2).take(2).padEnd(2, '_')
        val year = input.drop(4).take(4).padEnd(4, '_')
        val transformed = "$day/$month/$year"
        return TransformedText(
            text = AnnotatedString(transformed),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val o = offset.coerceIn(0, input.length)
                    return when {
                        o <= 1 -> o
                        o <= 3 -> o + 1
                        else -> o + 2
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val t = offset.coerceIn(0, 10)
                    val raw = when {
                        t <= 2 -> t
                        t <= 5 -> t - 1
                        else -> t - 2
                    }
                    return raw.coerceIn(0, input.length)
                }
            }
        )
    }
}
