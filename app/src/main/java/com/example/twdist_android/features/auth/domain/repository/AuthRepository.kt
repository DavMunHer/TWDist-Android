package com.example.twdist_android.features.auth.domain.repository

interface AuthRepository {
    // This is for defining the actions that this feature can do regarding the data layer (requests to back and stuff)

    fun createUser();
}