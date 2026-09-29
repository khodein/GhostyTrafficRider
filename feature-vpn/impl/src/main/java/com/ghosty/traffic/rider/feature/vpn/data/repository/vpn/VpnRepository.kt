package com.ghosty.traffic.rider.feature.vpn.data.repository.vpn

import android.content.Context
import android.content.Intent
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnStatus
import com.ghosty.traffic.rider.feature.vpn.service.ProxyVpnService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

internal class VpnRepository(
    private val context: Context,
) {
    private val mutableState = MutableStateFlow(VpnModel())

    val state = mutableState.asStateFlow()

    fun getStatusFlow() = state.map { it.status }

    fun start() {
        if (state.value.status != VpnStatus.Disconnected) return
        update(VpnModel(status = VpnStatus.Connecting))
        try {
            context.startForegroundService(Intent(context, ProxyVpnService::class.java))
        } catch (_: Exception) {
            update(VpnModel(error = "Не удалось запустить VPN-сервис"))
        }
    }

    fun stop() {
        if (state.value.status == VpnStatus.Disconnected) return
        context.startService(
            Intent(context, ProxyVpnService::class.java).setAction(ProxyVpnService.STOP)
        )
    }

    fun update(state: VpnModel) {
        mutableState.value = state
    }
}
