package com.ghosty.traffic.rider.feature.vpn.router

import com.ghosty.traffic.rider.feature.vpn.router.keys.VpnKey
import com.ghosty.traffic.rider.framework.router.Router

internal class VpnRouterImpl(
    private val router: Router,
) : VpnRouter {
    override fun goToVpn() {
        router.goTo(VpnKey)
    }
}
