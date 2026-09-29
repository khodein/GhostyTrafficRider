package com.ghosty.traffic.rider.feature.vpn.service

import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnExceptionModel

internal sealed interface VpnSessionEvent {
    data class Started(
        val profileName: String,
    ) : VpnSessionEvent

    data class Failed(
        val error: VpnExceptionModel,
    ) : VpnSessionEvent
}
