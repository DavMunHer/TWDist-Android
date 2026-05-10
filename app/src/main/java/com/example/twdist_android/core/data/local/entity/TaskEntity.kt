package com.example.twdist_android.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = SectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["section_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["section_id"])]
)
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "section_id") val sectionId: Long,
    @ColumnInfo(name = "name") val name: String,
    /** Kept for simple SQL filters aligned with backend "open task" semantics. Sync with persistence layer. */
    @ColumnInfo(name = "completed") val completed: Boolean,
    /** Calendar date when the task was marked complete (nullable when not completed). */
    @ColumnInfo(name = "completed_date") val completedDate: LocalDate? = null,
    @ColumnInfo(name = "description") val description: String?,
    /** Stored as epoch day INTEGER via [TwDistRoomConverters]. */
    @ColumnInfo(name = "start_date") val startDate: LocalDate? = null,
    @ColumnInfo(name = "end_date") val endDate: LocalDate? = null
)
