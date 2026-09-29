package com.ghosty.traffic.rider

import com.ghosty.traffic.rider.feature.selfprofile.di.ProfileModule
import com.ghosty.traffic.rider.db.AppDatabaseModule
import com.ghosty.traffic.rider.framework.tools.ResModule
import com.ghosty.traffic.rider.main.MainModule
import com.ghosty.traffic.rider.router.RouterModule
import com.ghosty.traffic.rider.feature.vpn.di.VpnModule
import org.koin.core.module.Module

internal object AppModule {

    fun get(): List<Module> {
        return listOf(
            RouterModule.get(),
            AppDatabaseModule.get(),
            MainModule.get(),
            ResModule.get(),
            ProfileModule.get(),
            VpnModule.get(),
        )
    }
}
