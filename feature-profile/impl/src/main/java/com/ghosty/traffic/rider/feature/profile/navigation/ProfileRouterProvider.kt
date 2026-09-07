package com.ghosty.traffic.rider.feature.profile.navigation

import com.ghosty.traffic.rider.feature.profile.presentation.ProfileScreen
import com.ghosty.traffic.rider.framework.router.EntryProviderInstaller
import com.ghosty.traffic.rider.framework.router.NavTransition
import com.ghosty.traffic.rider.framework.router.Router
import com.ghosty.traffic.rider.framework.router.navTransitionMetadata

internal class ProfileRouterProvider : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<ProfileRoute>(metadata = navTransitionMetadata(NavTransition.FADE)) {
            ProfileScreen()
        }
    }
}
