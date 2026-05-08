package com.example.twdist_android.core.events

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class TaskEventBus @Inject constructor() {
    private val _taskStartDateUpdated = MutableSharedFlow<TaskStartDateUpdatedEvent>()
    val taskStartDateUpdated: SharedFlow<TaskStartDateUpdatedEvent> = _taskStartDateUpdated

    suspend fun emitStartDateUpdated(event: TaskStartDateUpdatedEvent) {
        _taskStartDateUpdated.emit(event)
    }
}
