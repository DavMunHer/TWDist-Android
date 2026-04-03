package com.example.twdist_android.features.explore.domain.model

sealed class ProjectError {
    data object PendingTasksNegative : ProjectError()
    data object DuplicateSectionIds : ProjectError()
    data class SectionAlreadyLinked(val sectionId: Long) : ProjectError()
    data class SectionNotLinked(val sectionId: Long) : ProjectError()
}

@ConsistentCopyVisibility
data class Project private constructor(
    val id: Long,
    val name: ProjectName,
    val isFavorite: Boolean = false,
    val pendingTasks: Int = 0,
    val sectionIds: List<Long> = emptyList()
) {
    companion object {
        fun create(
            id: Long,
            name: ProjectName,
            isFavorite: Boolean = false,
            pendingTasks: Int = 0,
            sectionIds: List<Long> = emptyList()
        ): Result<Project> {
            if (pendingTasks < 0) {
                return Result.failure(ProjectException(ProjectError.PendingTasksNegative))
            }
            if (sectionIds.size != sectionIds.distinct().size) {
                return Result.failure(ProjectException(ProjectError.DuplicateSectionIds))
            }

            return Result.success(
                Project(
                    id = id,
                    name = name,
                    isFavorite = isFavorite,
                    pendingTasks = pendingTasks,
                    sectionIds = sectionIds
                )
            )
        }
    }

    fun linkSection(sectionId: Long): Result<Project> {
        if (sectionIds.contains(sectionId)) {
            return Result.failure(ProjectException(ProjectError.SectionAlreadyLinked(sectionId)))
        }
        return Result.success(copy(sectionIds = sectionIds + sectionId))
    }

    fun unlinkSection(sectionId: Long): Result<Project> {
        if (!sectionIds.contains(sectionId)) {
            return Result.failure(ProjectException(ProjectError.SectionNotLinked(sectionId)))
        }
        return Result.success(copy(sectionIds = sectionIds - sectionId))
    }
}

class ProjectException(val error: ProjectError) : IllegalArgumentException()