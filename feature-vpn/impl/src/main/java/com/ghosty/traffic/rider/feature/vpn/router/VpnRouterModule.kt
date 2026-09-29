package com.ghosty.traffic.rider.feature.vpn.router

import com.ghosty.traffic.rider.framework.router.Router
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

internal object VpnRouterModule {
    fun get(module: Module) = with(module) {
        addProviders()
        addRoutes()
    }

    private fun Module.addProviders() {
        single<Router.Provider> { VpnRouterProvider() }
    }

    private fun Module.addRoutes() {
        singleOf(::VpnRouterImpl) bind VpnRouter::class
    }
}
