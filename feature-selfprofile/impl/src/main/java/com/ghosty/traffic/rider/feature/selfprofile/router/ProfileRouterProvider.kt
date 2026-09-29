package com.ghosty.traffic.rider.feature.selfprofile.router

import androidx.compose.material3.ExperimentalMaterial3Api
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.ProfileListDialogScreen
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.ProfileListScreen
import com.ghosty.traffic.rider.feature.selfprofile.router.keys.ProfileEditorKey
import com.ghosty.traffic.rider.feature.selfprofile.router.keys.ProfileListDialogKey
import com.ghosty.traffic.rider.feature.selfprofile.router.keys.ProfileListKey
import com.ghosty.traffic.rider.framework.BottomSheetSceneStrategy
import com.ghosty.traffic.rider.framework.router.EntryProviderInstaller
import com.ghosty.traffic.rider.framework.router.NavTransition
import com.ghosty.traffic.rider.framework.router.Router
import com.ghosty.traffic.rider.framework.router.navTransitionMetadata

internal class ProfileRouterProvider : Router.Provider {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun invoke(): EntryProviderInstaller = {
        entry<ProfileListKey>(metadata = navTransitionMetadata(NavTransition.FADE)) {
            ProfileListScreen()
        }

        entry<ProfileListDialogKey>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) {
            ProfileListDialogScreen()
        }

        entry<ProfileEditorKey>(
            metadata = navTransitionMetadata(NavTransition.FADE)
        ) {

        }
    }
}
