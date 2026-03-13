package com.example.twdist_android.features.auth.domain.model.shared

import org.junit.Assert.assertTrue
import org.junit.Test

class EmailTest {

    @Test
    fun `given valid email, when create, then returns success`() {
        val result = Email.create("user@email.com")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given empty email, when create, then returns failure`() {
        val result = Email.create("")
        assertTrue(result.isFailure)
    }

    @Test
    fun `given email without at sign, when create, then returns failure`() {
        val result = Email.create("invalidemail.com")
        assertTrue(result.isFailure)
    }
}