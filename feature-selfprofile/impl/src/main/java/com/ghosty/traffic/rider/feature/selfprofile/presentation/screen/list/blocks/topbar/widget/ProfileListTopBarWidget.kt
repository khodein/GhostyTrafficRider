package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.state.ProfileListTopBarUiState
import com.ghosty.traffic.rider.framework.uikit.AppTopBarWidget

@Composable
internal fun ProfileListTopBarWidget(
    modifier: Modifier = Modifier,
    uiState: ProfileListTopBarUiState,
) {
    AppTopBarWidget(
        modifier = modifier.fillMaxWidth(),
        title = uiState.title,
        showBackButton = uiState.isBackPressed,
        onBackClick = uiState.backPressedClickEvent::invoke
    )
}