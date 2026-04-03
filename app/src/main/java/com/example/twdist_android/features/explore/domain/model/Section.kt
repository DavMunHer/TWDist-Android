package com.example.twdist_android.features.explore.domain.model

sealed class SectionError {
    data object TaskIdBlank : SectionError()
    data object DuplicateTaskIds : SectionError()
    data class TaskIdAlreadyLinked(val taskId: String) : SectionError()
    data class TaskIdNotLinked(val taskId: String) : SectionError()
    data class NewTaskIdAlreadyLinked(val taskId: String) : SectionError()
}

@ConsistentCopyVisibility
data class Section private constructor(
    val id: Long,
    val projectId: Long,
    val name: SectionName,
    val taskIds: List<String> = emptyList()
) {
    companion object {
        fun create(
            id: Long,
            projectId: Long,
            name: SectionName,
            taskIds: List<String> = emptyList()
        ): Result<Section> {
            if (taskIds.any { it.isBlank() }) {
                return Result.failure(SectionException(SectionError.TaskIdBlank))
            }
            if (taskIds.size != taskIds.distinct().size) {
                return Result.failure(SectionException(SectionError.DuplicateTaskIds))
            }

            return Result.success(
                Section(
                    id = id,
                    projectId = projectId,
                    name = name,
                    taskIds = taskIds
                )
            )
        }
    }

    fun addTaskId(taskId: String): Result<Section> {
        if (taskId.isBlank()) {
            return Result.failure(SectionException(SectionError.TaskIdBlank))
        }
        if (taskIds.contains(taskId)) {
            return Result.failure(SectionException(SectionError.TaskIdAlreadyLinked(taskId)))
        }

        return Result.success(copy(taskIds = taskIds + taskId))
    }

    fun removeTaskId(taskId: String): Result<Section> {
        if (!taskIds.contains(taskId)) {
            return Result.failure(SectionException(SectionError.TaskIdNotLinked(taskId)))
        }

        return Result.success(copy(taskIds = taskIds - taskId))
    }

    fun replaceTaskId(currentTaskId: String, newTaskId: String): Result<Section> {
        if (!taskIds.contains(currentTaskId)) {
            return Result.failure(SectionException(SectionError.TaskIdNotLinked(currentTaskId)))
        }
        if (newTaskId.isBlank()) {
            return Result.failure(SectionException(SectionError.TaskIdBlank))
        }
        if (taskIds.contains(newTaskId) && newTaskId != currentTaskId) {
            return Result.failure(SectionException(SectionError.NewTaskIdAlreadyLinked(newTaskId)))
        }

        val index = taskIds.indexOf(currentTaskId)
        val replaced = taskIds.toMutableList().apply { this[index] = newTaskId }
        return Result.success(copy(taskIds = replaced))
    }
}

class SectionException(val error: SectionError) : IllegalArgumentException()