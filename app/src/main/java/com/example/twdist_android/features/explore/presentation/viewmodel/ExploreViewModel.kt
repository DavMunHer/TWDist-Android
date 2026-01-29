package com.example.twdist_android.features.explore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.twdist_android.features.explore.domain.usecases.CreateProjectUseCase
import com.example.twdist_android.features.explore.domain.usecases.GetProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase
) : ViewModel() {
    // TODO: Implement logic to manage projects using the use cases
}