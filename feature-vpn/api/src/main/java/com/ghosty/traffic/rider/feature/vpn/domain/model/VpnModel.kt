package com.ghosty.traffic.rider.feature.vpn.domain.model

data class VpnModel(
    val status: VpnStatus = VpnStatus.Disconnected,
    val profileName: String? = null,
    val error: String? = null,
)
