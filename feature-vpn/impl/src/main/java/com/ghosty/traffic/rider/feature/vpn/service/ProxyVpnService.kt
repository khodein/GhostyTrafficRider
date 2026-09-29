package com.ghosty.traffic.rider.feature.vpn.service

import android.content.Intent
import android.net.VpnService
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Log
import com.ghosty.traffic.rider.feature.vpn.data.repository.vpn.VpnRepository
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnStatus
import com.ghosty.traffic.rider.feature.vpn.engine.VpnTunnelConfig
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProxyVpnService : VpnService() {
    private val proxyVpnScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val vpnRepository: VpnRepository by inject()
    private val vpnSession: VpnSession by inject()
    private val notificationFactory: VpnNotificationFactory by inject()
    private val stopping = AtomicBoolean(false)
    private var starting = false
    @Volatile
    private var cleaned = false
    private var finalError: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP) {
            requestStop()
            return START_NOT_STICKY
        }
        if (starting) return START_NOT_STICKY
        starting = true
        try {
            startForeground(NOTIFICATION_ID, notificationFactory.create(this))
        } catch (_: Exception) {
            finalError = "Не удалось показать уведомление VPN"
            cleaned = true
            vpnRepository.update(VpnModel(error = finalError))
            stopSelf()
            return START_NOT_STICKY
        }

        proxyVpnScope.launch {
            vpnSession.start(
                descriptor = getDescriptor(),
                callback = VpnTunCallback(this@ProxyVpnService, ::requestStop),
            ).collect { event ->
                when (event) {
                    is VpnSessionEvent.Started -> {
                        if (!stopping.get()) {
                            vpnRepository.update(
                                VpnModel(VpnStatus.Connected, event.profileName),
                            )
                        }
                    }

                    is VpnSessionEvent.Failed -> {
                        Log.e(TAG, "Не удалось запустить VPN", event.error)
                        stopping.set(true)
                        cleanup(
                            "Не удалось запустить VPN. " +
                                "Проверьте конфигурацию и повторите запуск",
                        )
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun getDescriptor(): ParcelFileDescriptor {
        val descriptor: ParcelFileDescriptor = Builder()
            .setSession("GhostyTrafficRider")
            .setMtu(VpnTunnelConfig.MTU)
            .addAddress(VpnTunnelConfig.ADDRESS, VpnTunnelConfig.PREFIX_LENGTH)
            .addRoute("0.0.0.0", 0)
            .addDnsServer(VpnTunnelConfig.DNS)
            .addDisallowedApplication(packageName)
            .establish()
            ?: error("Не удалось создать VPN-интерфейс")
        return descriptor
    }

    override fun onRevoke() {
        requestStop()
    }

    override fun onDestroy() {
        if (cleaned) {
            vpnRepository.update(VpnModel(error = finalError))
        } else {
            requestStop()
        }
        super.onDestroy()
    }

    private fun requestStop() {
        if (!stopping.compareAndSet(false, true)) return
        vpnRepository.update(vpnRepository.state.value.copy(status = VpnStatus.Stopping))
        proxyVpnScope.launch { cleanup(null) }
    }

    private fun cleanup(error: String?) {
        if (cleaned) return
        try {
            vpnSession.stop()
        } finally {
            stopForeground(STOP_FOREGROUND_REMOVE)
            finalError = error
            cleaned = true
            Handler(Looper.getMainLooper()).post { stopSelf() }
        }
    }

    companion object {
        const val STOP = "com.ghosty.traffic.rider.feature.vpn.STOP"
        private const val TAG = "ProxyVpnService"
        private const val NOTIFICATION_ID = 1001
        const val RUNTIME_DIRECTORY = "mihomo"
    }
}
