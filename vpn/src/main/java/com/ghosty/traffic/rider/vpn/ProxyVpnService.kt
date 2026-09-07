package com.ghosty.traffic.rider.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import java.net.InetSocketAddress
import android.os.ParcelFileDescriptor
import android.util.Log
import io.github.oviron.libmihomo.TunInterface
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class ProxyVpnService : VpnService() {
    companion object {
        const val STOP = "com.ghosty.traffic.rider.vpn.STOP"
        private const val CHANNEL = "vpn"
        private const val NOTIFICATION = 1001
        private val worker = Executors.newSingleThreadExecutor()
    }

    private val stopping = AtomicBoolean(false)
    private var starting = false
    @Volatile private var cleaned = false
    private var finalError: String? = null
    private var tun: ParcelFileDescriptor? = null
    private val engine = MihomoEngine()
    private val runtimeDirectory get() = File(cacheDir, "mihomo")

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP) {
            requestStop()
            return START_NOT_STICKY
        }
        if (starting) return START_NOT_STICKY
        starting = true
        val profile = VpnConnection.pendingProfile.getAndSet(null)
        if (profile == null) {
            stopSelf()
            return START_NOT_STICKY
        }
        try {
            startForeground(NOTIFICATION, notification())
        } catch (_: Exception) {
            VpnConnection.update(VpnState(error = "Не удалось показать уведомление VPN"))
            stopSelf()
            return START_NOT_STICKY
        }
        worker.execute {
            try {
                RuntimeConfigBuilder().write(profile, runtimeDirectory)
                engine.setup(applicationInfo.nativeLibraryDir, runtimeDirectory)
                if (stopping.get()) return@execute
                val descriptor = Builder()
                    .setSession("GhostyTrafficRider")
                    .setMtu(MihomoEngine.MTU)
                    .addAddress(MihomoEngine.ADDRESS, 30)
                    .addRoute("0.0.0.0", 0)
                    .addDnsServer(MihomoEngine.DNS)
                    .addDisallowedApplication(packageName)
                    .establish() ?: error("Разрешение VPN недоступно")
                tun = descriptor
                engine.start(descriptor, object : TunInterface {
                    override fun protect(fd: Int) {
                        if (!this@ProxyVpnService.protect(fd)) requestStop()
                    }
                    override fun resolverProcess(protocol: Int, source: String, target: String, uid: Int): String {
                        val result = runCatching {
                            val owner = if (Build.VERSION.SDK_INT >= 29) {
                                getSystemService(ConnectivityManager::class.java).getConnectionOwnerUid(
                                    protocol, socketAddress(source), socketAddress(target),
                                )
                            } else uid
                            owner to packageManager.getPackagesForUid(owner)?.firstOrNull().orEmpty()
                        }
                        Log.d(
                            "ProxyVpnService",
                            "resolverProcess proto=$protocol src=$source dst=$target uid=$uid -> " +
                                result.fold({ (owner, pkg) -> "owner=$owner pkg=$pkg" }, { "error=${it}" }),
                        )
                        return result.getOrNull()?.second.orEmpty()
                    }
                })
                if (!stopping.get()) VpnConnection.update(VpnState(VpnStatus.CONNECTED, profile.name))
            } catch (error: Exception) {
                Log.e("ProxyVpnService", "Не удалось запустить VPN", error)
                stopping.set(true)
                cleanup("Не удалось запустить VPN. Проверьте конфигурацию и повторите запуск")
            }
        }
        return START_NOT_STICKY
    }

    private fun requestStop() {
        if (!stopping.compareAndSet(false, true)) return
        VpnConnection.update(VpnConnection.state.value.copy(status = VpnStatus.STOPPING))
        worker.execute { cleanup(null) }
    }

    private fun cleanup(error: String?) {
        try { engine.stop() } finally {
            // detachFd() inside MihomoEngine.start() clears ownership on success, so this
            // is a no-op then; it only matters when start() throws before reaching detachFd().
            tun?.close()
            tun = null
            File(runtimeDirectory, "config.yaml").delete()
            stopForeground(STOP_FOREGROUND_REMOVE)
            finalError = error
            cleaned = true
            Handler(Looper.getMainLooper()).post { stopSelf() }
        }
    }

    override fun onRevoke() { requestStop() }

    override fun onDestroy() {
        if (cleaned) VpnConnection.update(VpnState(error = finalError)) else requestStop()
        super.onDestroy()
    }

    private fun socketAddress(value: String): InetSocketAddress {
        val separator = value.lastIndexOf(':')
        return InetSocketAddress(value.substring(0, separator).removePrefix("[").removeSuffix("]"), value.substring(separator + 1).toInt())
    }

    private fun notification(): Notification {
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(NotificationChannel(CHANNEL, "VPN", NotificationManager.IMPORTANCE_LOW))
        val stop = PendingIntent.getService(
            this, 0, Intent(this, ProxyVpnService::class.java).setAction(STOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        return Notification.Builder(this, CHANNEL)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle("GhostyTrafficRider VPN")
            .setContentText("VPN запущен")
            .setOngoing(true)
            .addAction(Notification.Action.Builder(null, "STOP", stop).build())
            .build()
    }
}
