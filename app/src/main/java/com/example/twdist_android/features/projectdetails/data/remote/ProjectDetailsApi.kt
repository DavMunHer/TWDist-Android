package com.example.twdist_android.features.projectdetails.data.remote

import com.example.twdist_android.features.projectdetails.data.dto.UpdateSectionRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import com.example.twdist_android.features.projectdetails.data.dto.ProjectDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT

interface ProjectDetailsApi {
    @GET("projects/{projectId}")
    suspend fun getProjectById(
        @Path("projectId") projectId: Long
    ): ProjectDetailResponseDto

    @PUT("projects/{projectId}/section/{sectionId}/update")
    suspend fun updateSection(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Body request: UpdateSectionRequestDto
    ): ResponseBody

    @DELETE("projects/{projectId}/section/{sectionId}/delete")
    suspend fun deleteSection(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long
    ): Response<Unit>
}
