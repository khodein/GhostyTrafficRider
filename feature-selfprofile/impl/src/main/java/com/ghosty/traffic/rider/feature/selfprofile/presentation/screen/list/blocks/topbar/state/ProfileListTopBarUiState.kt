package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.framework.stable.StableClickEvent

@Immutable
internal data class ProfileListTopBarUiState(
    val title: String,
    val isBackPressed: Boolean,
    val backPressedClickEvent: StableClickEvent,
)