package com.example.twdist_android.core.events

import java.time.LocalDate

data class TaskStartDateUpdatedEvent(val taskId: Long, val newStartDate: LocalDate?)
