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

class CreateSectionUseCaseTest {
    private lateinit var useCase: CreateSectionUseCase
    private val repository: SectionRepository = mockk()

    @Before
    fun setUp() {
        useCase = CreateSectionUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        val sectionName = SectionName.create("Todo").getOrThrow()
        val section = Section.create(1L, 10L, sectionName, emptyList()).getOrThrow()
        coEvery { repository.createSection(10L, sectionName) } returns Result.success(section)

        val result = useCase(10L, sectionName)

        coVerify(exactly = 1) { repository.createSection(10L, sectionName) }
        assertTrue(result.isSuccess)
        assertEquals(section, result.getOrThrow())
    }
}
