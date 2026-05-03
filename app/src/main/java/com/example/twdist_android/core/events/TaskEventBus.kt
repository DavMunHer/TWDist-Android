package com.example.twdist_android.core.events

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class TaskEventBus @Inject constructor() {
    private val _taskEndDateUpdated = MutableSharedFlow<TaskEndDateUpdatedEvent>()
    val taskEndDateUpdated: SharedFlow<TaskEndDateUpdatedEvent> = _taskEndDateUpdated

    suspend fun emitEndDateUpdated(taskId: Long, newEndDate: LocalDate?) {
        _taskEndDateUpdated.emit(TaskEndDateUpdatedEvent(taskId, newEndDate))
    }
}
