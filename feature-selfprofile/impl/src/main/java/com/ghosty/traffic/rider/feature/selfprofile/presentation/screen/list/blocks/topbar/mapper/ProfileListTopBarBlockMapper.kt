package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.mapper

import com.ghosty.traffic.rider.feature.selfprofile.impl.R
import com.ghosty.traffic.rider.framework.tools.res.ResProvider

internal class ProfileListTopBarBlockMapper(
    private val resProvider: ResProvider
) {
    fun getMyProfilesText(): String {
        return resProvider.getString(R.string.profile_my_profiles)
    }
}