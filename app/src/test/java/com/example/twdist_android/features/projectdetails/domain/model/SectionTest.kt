package com.example.twdist_android.features.projectdetails.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SectionTest {

    @Test
    fun `create should fail when task ids include blank values`() {
        val name = SectionName.create("Backlog").getOrThrow()

        val result = Section.create(
            id = 1L,
            projectId = 1L,
            name = name,
            taskIds = listOf("tmp_1", " ")
        )

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is SectionException &&
                (result.exceptionOrNull() as SectionException).error is SectionError.TaskIdBlank
        )
    }

    @Test
    fun `create should fail when task ids are duplicated`() {
        val name = SectionName.create("Backlog").getOrThrow()

        val result = Section.create(
            id = 1L,
            projectId = 1L,
            name = name,
            taskIds = listOf("tmp_1", "tmp_1")
        )

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is SectionException &&
                (result.exceptionOrNull() as SectionException).error is SectionError.DuplicateTaskIds
        )
    }

    @Test
    fun `replace task id should preserve order and replace only target`() {
        val name = SectionName.create("Backlog").getOrThrow()
        val section = Section.create(
            id = 1L,
            projectId = 1L,
            name = name,
            taskIds = listOf("tmp_1", "tmp_2", "tmp_3")
        ).getOrThrow()

        val result = section.replaceTaskId("tmp_2", "42")

        assertTrue(result.isSuccess)
        assertEquals(listOf("tmp_1", "42", "tmp_3"), result.getOrThrow().taskIds)
    }

    @Test
    fun `replace task id should fail when source id does not exist`() {
        val name = SectionName.create("Backlog").getOrThrow()
        val section = Section.create(
            id = 1L,
            projectId = 1L,
            name = name,
            taskIds = listOf("tmp_1")
        ).getOrThrow()

        val result = section.replaceTaskId("tmp_404", "42")

        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull() is SectionException &&
                (result.exceptionOrNull() as SectionException).error is SectionError.TaskIdNotLinked
        )
    }
}
