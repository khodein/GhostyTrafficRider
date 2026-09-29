package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState.ClickEvent
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState.Item
import com.ghosty.traffic.rider.framework.stable.StableEvent

@Immutable
internal data class ProfileUiState(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: String,
    val isSelected: Boolean,
    val clickEvent: StableEvent<ClickEvent>,
) : Item {

    override val contentType: String = PROFILE_CONTENT_TYPE
    override val key: String = "${id}${name}${type}"

    val description: String
        get() {
            return "$type · ${server}:${port}"
        }

    companion object {
        const val PROFILE_CONTENT_TYPE = "ProfileWidgetUiStateContentType"
    }
}