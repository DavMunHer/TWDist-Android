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

class UpdateSectionNameUseCaseTest {
    private lateinit var useCase: UpdateSectionNameUseCase
    private val repository: SectionRepository = mockk()

    @Before
    fun setUp() {
        useCase = UpdateSectionNameUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        val sectionName = SectionName.create("Done").getOrThrow()
        val section = Section.create(3L, 1L, sectionName, emptyList()).getOrThrow()
        coEvery { repository.updateSectionName(3L, sectionName) } returns Result.success(section)

        val result = useCase(3L, sectionName)

        coVerify(exactly = 1) { repository.updateSectionName(3L, sectionName) }
        assertTrue(result.isSuccess)
        assertEquals(section, result.getOrThrow())
    }
}
