package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.state.ProfileListBottomBarUiState
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.state.ProfileListTopBarUiState
import com.ghosty.traffic.rider.framework.UiState

@Immutable
internal data class ProfileListState(
    val profileListState: ProfileListUiState,
    val profileListBottomBarState: ProfileListBottomBarUiState,
    val profileListTopBarState: ProfileListTopBarUiState,
) : UiState()