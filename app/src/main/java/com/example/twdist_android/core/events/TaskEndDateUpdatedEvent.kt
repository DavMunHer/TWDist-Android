package com.example.twdist_android.core.events

import java.time.LocalDate

data class TaskEndDateUpdatedEvent(val taskId: Long, val newEndDate: LocalDate?)
