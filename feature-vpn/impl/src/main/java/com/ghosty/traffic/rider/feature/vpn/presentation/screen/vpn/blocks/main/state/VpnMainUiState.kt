package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.framework.stable.StableClickEvent

@Immutable
internal data class VpnMainUiState(
    val status: String,
    val profileName: String,
    val error: String?,
    val isStartVisible: Boolean,
    val isActionEnabled: Boolean,
    val actionClickEvent: StableClickEvent,
)
