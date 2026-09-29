package com.ghosty.traffic.rider.feature.vpn.di

import com.ghosty.traffic.rider.feature.vpn.data.VpnDataModule
import com.ghosty.traffic.rider.feature.vpn.domain.VpnDomainModule
import com.ghosty.traffic.rider.feature.vpn.engine.VpnEngineModule
import com.ghosty.traffic.rider.feature.vpn.presentation.VpnPresentationModule
import com.ghosty.traffic.rider.feature.vpn.router.VpnRouterModule
import com.ghosty.traffic.rider.feature.vpn.service.VpnServiceModule
import org.koin.dsl.module

object VpnModule {
    fun get() = module {
        VpnDataModule.get(this)
        VpnDomainModule.get(this)
        VpnEngineModule.get(this)
        VpnPresentationModule.get(this)
        VpnRouterModule.get(this)
        VpnServiceModule.get(this)
    }
}
