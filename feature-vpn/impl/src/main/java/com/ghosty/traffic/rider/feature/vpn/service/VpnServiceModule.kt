package com.ghosty.traffic.rider.feature.vpn.service

import com.ghosty.traffic.rider.feature.vpn.config.RuntimeConfigBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

internal object VpnServiceModule {
    fun get(module: Module) = with(module) {
        addServices()
    }

    private fun Module.addServices() {
        singleOf(::RuntimeConfigBuilder)
        singleOf(::VpnNotificationFactory)
        singleOf(::VpnSession)
    }
}
