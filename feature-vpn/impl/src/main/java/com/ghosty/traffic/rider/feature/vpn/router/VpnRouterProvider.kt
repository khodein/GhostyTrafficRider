package com.ghosty.traffic.rider.feature.vpn.router

import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.VpnScreen
import com.ghosty.traffic.rider.feature.vpn.router.keys.VpnKey
import com.ghosty.traffic.rider.framework.router.AppStartKey
import com.ghosty.traffic.rider.framework.router.EntryProviderInstaller
import com.ghosty.traffic.rider.framework.router.NavTransition
import com.ghosty.traffic.rider.framework.router.Router
import com.ghosty.traffic.rider.framework.router.navTransitionMetadata

internal class VpnRouterProvider : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<AppStartKey>(metadata = navTransitionMetadata(NavTransition.FADE)) {
            VpnScreen()
        }
        entry<VpnKey>(metadata = navTransitionMetadata(NavTransition.FADE)) {
            VpnScreen()
        }
    }
}
