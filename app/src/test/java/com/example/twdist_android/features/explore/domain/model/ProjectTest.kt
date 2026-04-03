package com.example.twdist_android.features.explore.domain.model

import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectTest {

    @Test
    fun `create should fail when pending tasks is negative`() {
        val name = ProjectName.create("Inbox").getOrThrow()

        val result = Project.create(
            id = 1L,
            name = name,
            pendingTasks = -1
        )

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is ProjectException &&
                (result.exceptionOrNull() as ProjectException).error is ProjectError.PendingTasksNegative
        )
    }

    @Test
    fun `create should fail when section ids contain duplicates`() {
        val name = ProjectName.create("Inbox").getOrThrow()

        val result = Project.create(
            id = 1L,
            name = name,
            sectionIds = listOf(10L, 10L)
        )

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is ProjectException &&
                (result.exceptionOrNull() as ProjectException).error is ProjectError.DuplicateSectionIds
        )
    }

    @Test
    fun `link section should fail when section id already exists`() {
        val name = ProjectName.create("Inbox").getOrThrow()
        val project = Project.create(
            id = 1L,
            name = name,
            sectionIds = listOf(10L)
        ).getOrThrow()

        val result = project.linkSection(10L)

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is ProjectException &&
                (result.exceptionOrNull() as ProjectException).error is ProjectError.SectionAlreadyLinked
        )
    }

    @Test
    fun `unlink section should fail when section id does not exist`() {
        val name = ProjectName.create("Inbox").getOrThrow()
        val project = Project.create(id = 1L, name = name).getOrThrow()

        val result = project.unlinkSection(99L)

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is ProjectException &&
                (result.exceptionOrNull() as ProjectException).error is ProjectError.SectionNotLinked
        )
    }
}
