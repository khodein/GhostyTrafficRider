package com.ghosty.traffic.rider.feature.vpn.engine

import android.os.ParcelFileDescriptor
import io.github.oviron.libmihomo.TunInterface
import java.io.File

internal interface MihomoEngine {
    suspend fun start(
        nativeDirectory: String,
        directory: File,
        descriptor: ParcelFileDescriptor,
        callback: TunInterface,
    ): Boolean

    fun stop()
}
