package com.ghosty.traffic.rider.feature.vpn.engine

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

internal object VpnEngineModule {
    fun get(module: Module) = with(module) {
        addEngine()
    }

    private fun Module.addEngine() {
        singleOf(::MihomoEngineImpl) bind MihomoEngine::class
    }
}
