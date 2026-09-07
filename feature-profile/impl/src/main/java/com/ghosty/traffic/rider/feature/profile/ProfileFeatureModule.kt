package com.ghosty.traffic.rider.feature.profile

import com.ghosty.traffic.rider.feature.profile.parser.ProxyProfileParser
import com.ghosty.traffic.rider.feature.profile.storage.FileProfileStorage
import com.ghosty.traffic.rider.feature.profile.storage.ProfileStorage
import com.ghosty.traffic.rider.feature.profile.navigation.ProfileRouterProvider
import com.ghosty.traffic.rider.feature.profile.presentation.ProfileListViewModel
import com.ghosty.traffic.rider.framework.router.Router
import org.koin.core.module.dsl.viewModelOf
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ProfileFeatureModule {
    fun get() = module {
        viewModelOf(::ProfileListViewModel)
        single<Router.Provider> { ProfileRouterProvider() }
        single { ProxyProfileParser() }
        single<ProfileStorage> { FileProfileStorage(androidContext().filesDir) }
    }
}
