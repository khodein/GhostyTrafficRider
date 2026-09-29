package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.mapper

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnStatus
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.state.VpnMainUiState
import com.ghosty.traffic.rider.framework.stable.StableClickEvent

internal class VpnMainBlockMapper {
    fun map(
        status: VpnStatus,
        profile: ProfileModel?,
        error: String?,
        onStart: () -> Unit,
        onStop: () -> Unit,
    ) = VpnMainUiState(
        status = when (status) {
            VpnStatus.Disconnected -> "Выключен"
            VpnStatus.Connecting -> "Подключение"
            VpnStatus.Connected -> "Включен"
            VpnStatus.Stopping -> "Отключение"
        },
        profileName = profile?.name ?: "Выберите профиль",
        error = error,
        isStartVisible = status == VpnStatus.Disconnected,
        isActionEnabled = when (status) {
            VpnStatus.Disconnected -> profile != null
            VpnStatus.Connected -> true
            VpnStatus.Connecting, VpnStatus.Stopping -> false
        },
        actionClickEvent = StableClickEvent(
            stableKey = "vpn_${status.name}",
            action = if (status == VpnStatus.Disconnected) onStart else onStop,
        ),
    )
}
