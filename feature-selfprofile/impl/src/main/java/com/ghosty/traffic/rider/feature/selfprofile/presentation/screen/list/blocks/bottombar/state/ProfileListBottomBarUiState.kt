package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.framework.stable.StableClickEvent

@Immutable
internal data class ProfileListBottomBarUiState(
    val caption: String,
    val isEnabled: Boolean,
    val stable: StableClickEvent,
)