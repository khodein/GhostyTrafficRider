package com.ghosty.traffic.rider.feature.vpn.presentation

import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.VpnViewModel
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.VpnMainBlock
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.mapper.VpnMainBlockMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf

internal object VpnPresentationModule {
    fun get(module: Module) = with(module) {
        addViewModels()
        addBlocks()
        addMappers()
    }

    private fun Module.addViewModels() {
        viewModelOf(::VpnViewModel)
    }

    private fun Module.addBlocks() {
        factoryOf(::VpnMainBlock)
    }

    private fun Module.addMappers() {
        factoryOf(::VpnMainBlockMapper)
    }
}
