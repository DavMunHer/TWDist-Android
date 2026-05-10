package com.example.twdist_android.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project row in SQLite.
 * Pending task counts come from aggregates over [task] rows (via queries), not from a stored column.
 */
@Entity(tableName = "project")
data class ProjectEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean
)
