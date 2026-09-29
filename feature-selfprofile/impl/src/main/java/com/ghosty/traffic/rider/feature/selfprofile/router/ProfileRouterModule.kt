package com.ghosty.traffic.rider.feature.selfprofile.router

import com.ghosty.traffic.rider.framework.router.Router
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

internal object ProfileRouterModule {

    fun get(module: Module) = with(module) {
        addProviders()
        addRoutes()
    }

    private fun Module.addProviders() {
        single<Router.Provider> { ProfileRouterProvider() }

    }

    private fun Module.addRoutes() {
        singleOf(::ProfileRouterImpl) bind ProfileRouter::class
    }
}