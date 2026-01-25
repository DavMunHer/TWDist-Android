package com.example.twdist_android.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {
    fun greeting(): String = "Hello from Repository"
}
