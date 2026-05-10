package com.example.twdist_android.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.twdist_android.core.data.local.dao.ProjectDao
import com.example.twdist_android.core.data.local.dao.SectionDao
import com.example.twdist_android.core.data.local.dao.TaskDao
import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.core.data.local.entity.SectionEntity
import com.example.twdist_android.core.data.local.entity.TaskEntity

@Database(
    entities = [
        ProjectEntity::class,
        SectionEntity::class,
        TaskEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class TWDistDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun sectionDao(): SectionDao
    abstract fun taskDao(): TaskDao
}
