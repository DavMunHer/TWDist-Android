package com.example.twdist_android.features.projectdetails.presentation.model

data class ProjectDetailsUiState(
    val isLoading: Boolean = true,
    val project: ProjectDetailsUi? = null,
    val error: String? = null,
    val openSectionMenuForId: Long? = null,
    val editingSectionId: Long? = null,
    val editingSectionName: String = "",
    val sectionActionError: String? = null,
    val deleteConfirmSectionId: Long? = null
)

data class ProjectDetailsUi(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val sections: List<SectionUi>
)

data class SectionUi(
    val id: Long,
    val name: String,
    val tasks: List<String>
)
