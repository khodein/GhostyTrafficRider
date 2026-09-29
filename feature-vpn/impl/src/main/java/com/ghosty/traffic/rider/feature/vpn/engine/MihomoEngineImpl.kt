package com.ghosty.traffic.rider.feature.vpn.engine

import android.os.Build
import android.os.ParcelFileDescriptor
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnEngineConfigRejectedExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnEngineSetupTimeoutExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnEngineUnavailableExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnTunStartExceptionModel
import io.github.oviron.libmihomo.Clash
import io.github.oviron.libmihomo.InvokeInterface
import io.github.oviron.libmihomo.TunInterface
import org.json.JSONObject
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout

internal class MihomoEngineImpl : MihomoEngine {

    override suspend fun start(
        nativeDirectory: String,
        directory: File,
        descriptor: ParcelFileDescriptor,
        callback: TunInterface,
    ): Boolean {
        setup(nativeDirectory, directory)
        startTun(descriptor, callback)
        return true
    }

    private suspend fun setup(nativeDirectory: String, directory: File) {
        Clash.load(nativeDirectory)
        Clash.assertReady()
        val result = try {
            withTimeout(SETUP_TIMEOUT_MILLIS) {
                suspendCancellableCoroutine { continuation ->
                    val json = JSONObject()
                        .put("home-dir", directory.absolutePath)
                        .put("version", Build.VERSION.SDK_INT)
                    Clash.quickSetup(
                        json.toString(),
                        "{}",
                    ) { result ->
                        if (continuation.isActive) {
                            continuation.resume(result)
                        } else {
                            Clash.stopTun()
                        }
                    }
                }
            }
        } catch (_: TimeoutCancellationException) {
            throw VpnEngineSetupTimeoutExceptionModel()
        }
        if (!result.isNullOrEmpty()) {
            throw VpnEngineConfigRejectedExceptionModel()
        }
    }

    private fun startTun(descriptor: ParcelFileDescriptor, callback: TunInterface) {
        val started = CountDownLatch(1)
        val success = AtomicBoolean(false)
        Clash.setEventListener { event: String? ->
            if (event?.contains("TUN started:") == true) {
                success.set(true)
                started.countDown()
            } else if (event?.contains("TUN start failed:") == true) {
                started.countDown()
            }
        }
        val subscribed = CountDownLatch(1)
        Clash.invokeAction("""{"id":"vpn-logs","method":"startLog"}""") { subscribed.countDown() }
        if (!subscribed.await(5, TimeUnit.SECONDS)) {
            throw VpnEngineUnavailableExceptionModel()
        }
        Clash.startTUN(
            descriptor.detachFd(),
            callback,
            "GhostyTrafficRider",
            "gvisor",
            "${VpnTunnelConfig.ADDRESS}/${VpnTunnelConfig.PREFIX_LENGTH}",
            VpnTunnelConfig.DNS,
            VpnTunnelConfig.MTU,
        )
        if (!started.await(10, TimeUnit.SECONDS) || !success.get()) {
            throw VpnTunStartExceptionModel()
        }
    }

    override fun stop() {
        if (Clash.isLoaded()) {
            Clash.stopTun()
            Clash.setEventListener(null as InvokeInterface?)
            Clash.invokeAction("""{"id":"vpn-stop-logs","method":"stopLog"}""") { }
        }
    }

    private companion object {
        const val SETUP_TIMEOUT_MILLIS = 30_000L
    }
}
