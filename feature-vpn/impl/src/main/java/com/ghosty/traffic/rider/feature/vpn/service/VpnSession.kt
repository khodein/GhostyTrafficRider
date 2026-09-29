package com.ghosty.traffic.rider.feature.vpn.service

import android.content.Context
import android.net.VpnService
import android.os.ParcelFileDescriptor
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetActiveProfileFlowUseCase
import com.ghosty.traffic.rider.feature.vpn.config.RuntimeConfigBuilder
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnActiveProfileMissingExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnTunStartExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnUnknownExceptionModel
import com.ghosty.traffic.rider.feature.vpn.engine.MihomoEngine
import com.ghosty.traffic.rider.feature.vpn.service.ProxyVpnService.Companion.RUNTIME_DIRECTORY
import java.io.File
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class VpnSession(
    private val getActiveProfileFlowUseCase: GetActiveProfileFlowUseCase,
    private val runtimeConfigBuilder: RuntimeConfigBuilder,
    private val engine: MihomoEngine,
    private val context: Context,
) {
    private var runtimeDirectory: File? = null

    fun start(
        descriptor: ParcelFileDescriptor,
        callback: VpnTunCallback,
    ): Flow<VpnSessionEvent> = flow {
        try {
            val profile = getActiveProfileFlowUseCase().value
                ?: throw VpnActiveProfileMissingExceptionModel()
            val directory = File(context.cacheDir, RUNTIME_DIRECTORY)
                .also { runtimeDirectory = it }

            runtimeConfigBuilder.write(profile, directory)

            val started = engine.start(
                nativeDirectory = context.applicationInfo.nativeLibraryDir,
                directory = directory,
                descriptor = descriptor,
                callback = callback,
            )
            if (!started) throw VpnTunStartExceptionModel()

            emit(VpnSessionEvent.Started(profile.name))
        } catch (error: CancellationException) {
            runCatching { descriptor.close() }
            throw error
        } catch (error: Exception) {
            runCatching { descriptor.close() }
            emit(
                VpnSessionEvent.Failed(
                    error = error as? VpnExceptionModel
                        ?: VpnUnknownExceptionModel(error),
                ),
            )
        }
    }

    fun stop() {
        try {
            engine.stop()
        } finally {
            runtimeDirectory?.resolve(CONFIG_FILE)?.delete()
            runtimeDirectory = null
        }
    }

    private companion object {
        const val CONFIG_FILE = "config.yaml"
    }
}
