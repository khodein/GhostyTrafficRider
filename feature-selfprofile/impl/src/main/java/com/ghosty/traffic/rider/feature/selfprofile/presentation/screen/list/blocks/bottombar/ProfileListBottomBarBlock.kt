package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar

import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.mapper.ProfileListBottomBarBlockMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.state.ProfileListBottomBarUiState
import com.ghosty.traffic.rider.framework.block.Block
import com.ghosty.traffic.rider.framework.stable.StableClickEvent

internal class ProfileListBottomBarBlock(
    private val profileListBottomBarMapper: ProfileListBottomBarBlockMapper,
) : Block<ProfileListBottomBarUiState, Unit>() {

    override fun getInitialUiState(): ProfileListBottomBarUiState {
        return ProfileListBottomBarUiState(
            caption = profileListBottomBarMapper.getAddNewProfileText(),
            isEnabled = true,
            stable = StableClickEvent(
                stableKey = BOTTOM_BAR_STABLE_CLICK_EVENT_ADD_NEW_PROFILE,
                action = ::addNewProfile
            )
        )
    }

    private fun addNewProfile() {
        // Добавить переход на экран добавление нового профиля
    }

    private companion object {
        const val BOTTOM_BAR_STABLE_CLICK_EVENT_ADD_NEW_PROFILE = "BOTTOM_BAR_STABLE_CLICK_EVENT_ADD_NEW_PROFILE"
    }
}