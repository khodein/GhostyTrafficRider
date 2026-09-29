package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.mapper

import com.ghosty.traffic.rider.feature.selfprofile.impl.R
import com.ghosty.traffic.rider.framework.tools.res.ResProvider

internal class ProfileListBottomBarBlockMapper(
    private val resProvider: ResProvider,
) {
    fun getAddNewProfileText(): String {
        return resProvider.getString(R.string.profile_add_new)
    }
}