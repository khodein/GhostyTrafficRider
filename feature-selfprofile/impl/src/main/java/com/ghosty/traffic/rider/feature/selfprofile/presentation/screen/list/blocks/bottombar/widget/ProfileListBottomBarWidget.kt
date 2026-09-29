package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.state.ProfileListBottomBarUiState
import com.ghosty.traffic.rider.framework.theme.AppTheme
import com.ghosty.traffic.rider.framework.uikit.AppButtonWidget

@Composable
internal fun ProfileListBottomBarWidget(
    modifier: Modifier = Modifier,
    uiState: ProfileListBottomBarUiState,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = AppTheme.color.surface)
            .padding(all = AppTheme.padding.small())
    ) {
        AppButtonWidget(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.caption,
            enabled = uiState.isEnabled,
            onClick = uiState.stable::invoke
        )
    }
}