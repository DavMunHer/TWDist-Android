package com.example.twdist_android.features.projectdetails.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskNameTest {

    @Test
    fun `create task name with valid input should succeed`() {
        val result = TaskName.create("My Task")
        assertTrue("Valid task name should succeed", result.isSuccess)
        assertEquals("My Task", result.getOrNull()?.asString())
    }

    @Test
    fun `create task name with too short input should fail`() {
        val result = TaskName.create("A")
        assertTrue("Too short task name should fail", result.isFailure)
        assertTrue(
            "Should be TaskNameException with TooShort error",
            result.exceptionOrNull() is TaskNameException &&
                (result.exceptionOrNull() as TaskNameException).error is TaskNameError.TooShort
        )
    }

    @Test
    fun `create task name with too long input should fail`() {
        val longName = "A".repeat(51)
        val result = TaskName.create(longName)
        assertTrue("Too long task name should fail", result.isFailure)
        assertTrue(
            "Should be TaskNameException with TooLong error",
            result.exceptionOrNull() is TaskNameException &&
                (result.exceptionOrNull() as TaskNameException).error is TaskNameError.TooLong
        )
    }
}
