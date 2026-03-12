package com.example.twdist_android.features.explore.domain.model

import org.junit.Test
import org.junit.Assert.*

class ProjectNameTest {

    @Test
    fun `create project name with valid input should succeed`() {
        val result = ProjectName.create("My Project")
        assertTrue("Valid project name should succeed", result.isSuccess)
        assertEquals("My Project", result.getOrNull()?.asString())
    }

    @Test
    fun `create project name with too short input should fail`() {
        val result = ProjectName.create("A")
        assertTrue("Too short project name should fail", result.isFailure)
        assertTrue(
            "Should be ProjectNameException with TooShort error",
            result.exceptionOrNull() is ProjectNameException &&
            (result.exceptionOrNull() as ProjectNameException).error is ProjectNameError.TooShort
        )
    }

    @Test
    fun `create project name with too long input should fail`() {
        val longName = "A".repeat(51)
        val result = ProjectName.create(longName)
        assertTrue("Too long project name should fail", result.isFailure)
        assertTrue(
            "Should be ProjectNameException with TooLong error",
            result.exceptionOrNull() is ProjectNameException &&
            (result.exceptionOrNull() as ProjectNameException).error is ProjectNameError.TooLong
        )
    }

    @Test
    fun `create project name with exactly 2 characters should succeed`() {
        val result = ProjectName.create("AB")
        assertTrue("Project name with 2 characters should succeed", result.isSuccess)
    }

    @Test
    fun `create project name with exactly 50 characters should succeed`() {
        val name = "A".repeat(50)
        val result = ProjectName.create(name)
        assertTrue("Project name with 50 characters should succeed", result.isSuccess)
    }

    @Test
    fun `create project name should trim whitespace`() {
        val result = ProjectName.create("  My Project  ")
        assertTrue("Trimmed project name should succeed", result.isSuccess)
        assertEquals("My Project", result.getOrNull()?.asString())
    }

    @Test
    fun `create project name with only whitespace should fail`() {
        val result = ProjectName.create("   ")
        assertTrue("Whitespace-only project name should fail", result.isFailure)
    }
}
