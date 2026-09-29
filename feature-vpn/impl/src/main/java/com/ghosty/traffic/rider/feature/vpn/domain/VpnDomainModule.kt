package com.ghosty.traffic.rider.feature.vpn.domain

import com.ghosty.traffic.rider.feature.vpn.domain.usecase.GetVpnStatusFlowUseCase
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.GetVpnStatusFlowUseCaseImpl
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.StartVpnUseCase
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.StartVpnUseCaseImpl
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.StopVpnUseCase
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.StopVpnUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

internal object VpnDomainModule {
    fun get(module: Module) = with(module) {
        addUseCases()
    }

    private fun Module.addUseCases() {
        factoryOf(::GetVpnStatusFlowUseCaseImpl) bind GetVpnStatusFlowUseCase::class
        factoryOf(::StartVpnUseCaseImpl) bind StartVpnUseCase::class
        factoryOf(::StopVpnUseCaseImpl) bind StopVpnUseCase::class
    }
}
