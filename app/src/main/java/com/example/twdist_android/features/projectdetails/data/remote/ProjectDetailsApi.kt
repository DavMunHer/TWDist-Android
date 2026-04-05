package com.example.twdist_android.features.projectdetails.data.remote

import com.example.twdist_android.features.projectdetails.data.dto.ProjectDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProjectDetailsApi {
    @GET("projects/{projectId}")
    suspend fun getProjectById(
        @Path("projectId") projectId: Long
    ): ProjectDetailResponseDto
}
