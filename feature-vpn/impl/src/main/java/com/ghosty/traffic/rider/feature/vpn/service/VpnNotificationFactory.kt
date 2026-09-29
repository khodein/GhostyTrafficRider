package com.ghosty.traffic.rider.feature.vpn.service

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent

internal class VpnNotificationFactory {
    fun create(service: ProxyVpnService): Notification {
        val manager = service.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL, "VPN", NotificationManager.IMPORTANCE_LOW)
        )
        val stop = PendingIntent.getService(
            service,
            0,
            Intent(service, ProxyVpnService::class.java).setAction(ProxyVpnService.STOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        return Notification.Builder(service, CHANNEL)
            .setSmallIcon(R.drawable.stat_sys_warning)
            .setContentTitle("GhostyTrafficRider VPN")
            .setContentText("VPN запущен")
            .setOngoing(true)
            .addAction(Notification.Action.Builder(null, "STOP", stop).build())
            .build()
    }

    private companion object {
        const val CHANNEL = "vpn"
    }
}
