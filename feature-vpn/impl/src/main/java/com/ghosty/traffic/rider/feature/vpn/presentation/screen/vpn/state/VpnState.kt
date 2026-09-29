package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.state.VpnMainUiState
import com.ghosty.traffic.rider.framework.UiState

@Immutable
internal data class VpnState(
    val main: VpnMainUiState,
) : UiState()
