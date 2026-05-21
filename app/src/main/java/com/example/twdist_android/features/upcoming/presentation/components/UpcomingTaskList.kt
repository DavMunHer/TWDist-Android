package com.example.twdist_android.features.upcoming.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.core.ui.components.task.TaskRow
import com.example.twdist_android.core.ui.components.task.TaskRowState
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingListItem
import java.time.LocalDate

private fun previousHeaderDate(items: List<UpcomingListItem>, beforeIndex: Int): LocalDate? =
    items.take(beforeIndex)
        .asReversed()
        .firstNotNullOfOrNull { (it as? UpcomingListItem.Header)?.date }

@Composable
fun UpcomingTaskList(
    items: List<UpcomingListItem>,
    listState: LazyListState,
    onTaskCompleted: (TaskRowState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(items, key = { idx, item ->
            when (item) {
                is UpcomingListItem.Header -> "header_${item.date}"
                is UpcomingListItem.Task -> "task_${item.state.id}"
                is UpcomingListItem.PaddingDay -> "padding_${item.date}"
            }
        }) { idx, item ->
            when (item) {
                is UpcomingListItem.Header -> {
                    if (idx > 0) {
                        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                    }
                    val prevDate = previousHeaderDate(items, idx)
                    DayHeader(
                        date = item.date,
                        showMonthSeparator = prevDate != null && prevDate.month != item.date.month
                    )
                }
                is UpcomingListItem.PaddingDay -> {
                    if (idx > 0) {
                        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                    }
                    DayHeader(date = item.date, isPadding = true)
                }
                is UpcomingListItem.Task -> TaskRow(
                    task = item.state,
                    onCompleted = onTaskCompleted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}
