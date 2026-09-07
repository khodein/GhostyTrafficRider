package com.ghosty.traffic.rider.vpn

import android.os.Build
import android.os.ParcelFileDescriptor
import io.github.oviron.libmihomo.Clash
import io.github.oviron.libmihomo.InvokeInterface
import io.github.oviron.libmihomo.TunInterface
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import org.json.JSONObject

internal class MihomoEngine {
    companion object {
        @Volatile private var setupTimedOut = false
        const val ADDRESS = "172.19.0.1"
        const val DNS = "172.19.0.2"
        const val MTU = 1400
    }

    fun setup(nativeDirectory: String, directory: File) {
        check(!setupTimedOut) { "Перезапустите приложение после тайм-аута Mihomo" }
        Clash.load(nativeDirectory)
        Clash.assertReady()
        val complete = CountDownLatch(1)
        val success = AtomicBoolean(false)
        // v0.3.1 reads home-dir/config.yaml; setupParams.profile is ignored by the core.
        // InitParams.HomeDir is tagged json:"home-dir" (see oviron/libmihomo-android constant.go).
        Clash.quickSetup(
            JSONObject().put("home-dir", directory.absolutePath).put("version", Build.VERSION.SDK_INT).toString(),
            "{}",
        ) { result ->
            success.set(result.isNullOrEmpty())
            complete.countDown()
            if (setupTimedOut) Clash.stopTun()
        }
        if (!complete.await(30, TimeUnit.SECONDS)) {
            setupTimedOut = true
            error("Mihomo не завершил настройку. Перезапустите приложение")
        }
        check(success.get()) { "Mihomo отклонил конфигурацию. Проверьте параметры сервера" }
    }

    fun start(descriptor: ParcelFileDescriptor, callback: TunInterface) {
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
        check(subscribed.await(5, TimeUnit.SECONDS)) { "Mihomo не отвечает" }
        Clash.startTUN(descriptor.detachFd(), callback, "GhostyTrafficRider", "gvisor", "$ADDRESS/30", DNS, MTU)
        check(started.await(10, TimeUnit.SECONDS) && success.get()) { "Не удалось запустить TUN" }
    }

    fun stop() {
        if (Clash.isLoaded()) {
            Clash.stopTun()
            Clash.setEventListener(null as InvokeInterface?)
            Clash.invokeAction("""{"id":"vpn-stop-logs","method":"stopLog"}""") { }
        }
    }
}
