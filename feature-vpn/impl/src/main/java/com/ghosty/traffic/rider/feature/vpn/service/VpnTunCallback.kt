package com.ghosty.traffic.rider.feature.vpn.service

import android.net.ConnectivityManager
import android.os.Build
import android.net.VpnService
import io.github.oviron.libmihomo.TunInterface
import java.net.InetSocketAddress

internal class VpnTunCallback(
    private val service: VpnService,
    private val onProtectFailure: () -> Unit,
) : TunInterface {

    override fun protect(fd: Int) {
        if (!service.protect(fd)) onProtectFailure()
    }

    override fun resolverProcess(
        protocol: Int,
        source: String,
        target: String,
        uid: Int,
    ): String = runCatching {
        val owner = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            service.getSystemService(ConnectivityManager::class.java).getConnectionOwnerUid(
                protocol,
                socketAddress(source),
                socketAddress(target),
            )
        } else {
            uid
        }
        service.packageManager.getPackagesForUid(owner)?.firstOrNull().orEmpty()
    }.getOrDefault("")

    private fun socketAddress(value: String): InetSocketAddress {
        val separator = value.lastIndexOf(':')
        return InetSocketAddress(
            value.substring(0, separator).removePrefix("[").removeSuffix("]"),
            value.substring(separator + 1).toInt(),
        )
    }
}
