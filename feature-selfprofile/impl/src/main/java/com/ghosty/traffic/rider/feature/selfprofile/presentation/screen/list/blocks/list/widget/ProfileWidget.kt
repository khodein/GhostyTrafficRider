package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileUiState
import com.ghosty.traffic.rider.framework.theme.AppTheme

@Composable
internal fun ProfileWidget(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = AppTheme.color.surface,
                shape = AppTheme.corner.all()
            )
            .padding(all = AppTheme.padding.normal()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = uiState.isSelected,
            onClick = { uiState.clickEvent.invoke(ProfileListUiState.ClickEvent.Select) },
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier,
                text = uiState.name,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.color.onSurface
            )
            Text(
                modifier = Modifier,
                text = uiState.description,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.color.onSurfaceVariant
            )
        }
        IconButton(
            onClick = { uiState.clickEvent.invoke(ProfileListUiState.ClickEvent.Edit) }
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Редактировать профиль")
        }
        IconButton(
            onClick = { uiState.clickEvent.invoke(ProfileListUiState.ClickEvent.Delete) }
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить профиль")
        }
    }
}