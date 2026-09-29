package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar

import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.mapper.ProfileListTopBarBlockMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.state.ProfileListTopBarUiState
import com.ghosty.traffic.rider.framework.block.Block
import com.ghosty.traffic.rider.framework.stable.StableClickEvent

internal class ProfileListTopBarBlock(
    private val profileListTopBarBlockMapper: ProfileListTopBarBlockMapper,
) : Block<ProfileListTopBarUiState, Unit>() {

    override fun getInitialUiState(): ProfileListTopBarUiState {
        return ProfileListTopBarUiState(
            title = profileListTopBarBlockMapper.getMyProfilesText(),
            isBackPressed = true,
            backPressedClickEvent = StableClickEvent(
                stableKey = PROFILE_LIST_TOP_BAR_STABLE_CLICK_BACK_PRESSED_EVENT,
                action = ::backPressed
            )
        )
    }

    private fun backPressed() {

    }

    private companion object {
        const val PROFILE_LIST_TOP_BAR_STABLE_CLICK_BACK_PRESSED_EVENT =
            "PROFILE_LIST_TOP_BAR_STABLE_CLICK_BACK_PRESSED_EVENT"
    }
}