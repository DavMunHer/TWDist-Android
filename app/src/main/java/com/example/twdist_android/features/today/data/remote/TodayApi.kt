package com.example.twdist_android.features.today.data.remote

import com.example.twdist_android.features.projectdetails.data.dto.task.CompleteTaskRequestDto
import com.example.twdist_android.features.today.data.dto.TodayTaskResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface TodayApi {
    @GET("tasks/today")
    suspend fun getTodayTasks(): Response<List<TodayTaskResponseDto>>

    @PATCH("projects/{projectId}/section/{sectionId}/task/{taskId}/complete")
    suspend fun completeTask(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Path("taskId") taskId: Long,
        @Body request: CompleteTaskRequestDto
    ): Response<Unit>
}
