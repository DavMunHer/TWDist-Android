package com.example.twdist_android.features.projectdetails.application.usecases.section

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReplaceTaskIdInSectionUseCaseTest {
    private lateinit var useCase: ReplaceTaskIdInSectionUseCase
    private val repository: SectionRepository = mockk()

    @Before
    fun setUp() {
        useCase = ReplaceTaskIdInSectionUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        val name = SectionName.create("Sec").getOrThrow()
        val section = Section.create(1L, 10L, name, listOf("new")).getOrThrow()
        coEvery { repository.replaceTaskIdInSection(1L, "old", "new") } returns Result.success(section)

        val result = useCase(1L, "old", "new")

        coVerify(exactly = 1) { repository.replaceTaskIdInSection(1L, "old", "new") }
        assertTrue(result.isSuccess)
        assertEquals(section, result.getOrThrow())
    }
}
