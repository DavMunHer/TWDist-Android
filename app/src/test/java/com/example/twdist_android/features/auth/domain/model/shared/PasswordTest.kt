package com.example.twdist_android.features.auth.domain.model.shared

import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordTest {

    @Test
    fun `given valid password, when create, then returns success`() {
        val result = Password.create("password123")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given blank password, when create, then returns failure`() {
        val result = Password.create("")
        assertTrue(result.isFailure)
    }

    @Test
    fun `given password too short, when create, then returns failure`() {
        val result = Password.create("abc")
        assertTrue(result.isFailure)
    }
}