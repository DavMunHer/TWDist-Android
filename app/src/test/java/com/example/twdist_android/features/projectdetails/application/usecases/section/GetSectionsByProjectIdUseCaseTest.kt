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

class GetSectionsByProjectIdUseCaseTest {
    private lateinit var useCase: GetSectionsByProjectIdUseCase
    private val repository: SectionRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetSectionsByProjectIdUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        val name = SectionName.create("S1").getOrThrow()
        val sections = listOf(Section.create(1L, 10L, name, emptyList()).getOrThrow())
        coEvery { repository.getSectionsByProjectId(10L) } returns Result.success(sections)

        val result = useCase(10L)

        coVerify(exactly = 1) { repository.getSectionsByProjectId(10L) }
        assertTrue(result.isSuccess)
        assertEquals(sections, result.getOrThrow())
    }
}
