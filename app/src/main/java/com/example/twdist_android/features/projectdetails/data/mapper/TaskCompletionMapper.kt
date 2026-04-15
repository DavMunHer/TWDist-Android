package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.data.dto.CompleteTaskRequestDto
import java.time.LocalDate

fun LocalDate?.toCompleteTaskRequestDto(): CompleteTaskRequestDto {
    return CompleteTaskRequestDto(completedDate = this?.toString())
}
