package com.example.twdist_android.features.projectdetails.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi

@Composable
internal fun SectionHeader(
    section: SectionUi,
    openSectionMenuForId: Long?,
    onSectionEvent: (SectionEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 2.dp)
        ) {
            Text(
                text = section.name,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { onSectionEvent(SectionEvent.MenuOpened(section.id)) }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Section Options",
                        modifier = Modifier.size(20.dp)
                    )
                }
                DropdownMenu(
                    expanded = openSectionMenuForId == section.id,
                    onDismissRequest = { onSectionEvent(SectionEvent.MenuDismissed) }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        onClick = { onSectionEvent(SectionEvent.EditClicked(section.id)) }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        onClick = { onSectionEvent(SectionEvent.DeleteClicked(section.id)) }
                    )
                }
            }
        }
    }
}
