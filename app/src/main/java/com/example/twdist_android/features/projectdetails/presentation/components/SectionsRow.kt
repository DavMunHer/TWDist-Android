package com.example.twdist_android.features.projectdetails.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState

@Composable
fun SectionsRow(
    modifier: Modifier = Modifier,
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit,
    onTaskEvent: (TaskEvent) -> Unit,
    onTaskClick: (sectionId: Long, taskId: Long) -> Unit
) {
    val listState = rememberLazyListState()
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.sectionItems) { section ->
            SectionCard(
                section = section,
                uiState = uiState,
                onSectionEvent = onSectionEvent,
                onTaskEvent = onTaskEvent,
                onTaskClick = onTaskClick,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        items(1) {
            SectionAdderCard(
                modifier = Modifier.fillParentMaxWidth(),
                onSectionEvent = onSectionEvent
            )
        }
    }
}
