package com.ghosty.traffic.rider.feature.vpn.data

import com.ghosty.traffic.rider.feature.vpn.data.repository.vpn.VpnRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

internal object VpnDataModule {
    fun get(module: Module) = with(module) {
        addRepository()
    }

    private fun Module.addRepository() {
        singleOf(::VpnRepository)
    }
}
