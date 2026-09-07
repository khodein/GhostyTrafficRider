package com.ghosty.traffic.rider.vpn

import android.content.Context
import android.content.Intent
import com.ghosty.traffic.rider.feature.profile.model.ProxyProfile
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class VpnStatus { DISCONNECTED, CONNECTING, CONNECTED, STOPPING }

data class VpnState(
    val status: VpnStatus = VpnStatus.DISCONNECTED,
    val profileName: String? = null,
    val error: String? = null,
)

object VpnConnection {
    private val mutableState = MutableStateFlow(VpnState())
    val state = mutableState.asStateFlow()
    internal val pendingProfile = AtomicReference<ProxyProfile?>(null)

    fun start(context: Context, profile: ProxyProfile) {
        if (state.value.status != VpnStatus.DISCONNECTED) return
        pendingProfile.set(profile)
        update(VpnState(VpnStatus.CONNECTING, profile.name))
        try {
            context.startForegroundService(Intent(context, ProxyVpnService::class.java))
        } catch (_: Exception) {
            pendingProfile.set(null)
            update(VpnState(error = "Не удалось запустить VPN-сервис"))
        }
    }

    fun stop(context: Context) {
        if (state.value.status == VpnStatus.DISCONNECTED) return
        context.startService(Intent(context, ProxyVpnService::class.java).setAction(ProxyVpnService.STOP))
    }

    internal fun update(state: VpnState) { mutableState.value = state }
}
