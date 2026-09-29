package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state

import androidx.compose.runtime.Immutable
import com.ghosty.traffic.rider.framework.UiStatus
import com.ghosty.traffic.rider.framework.stable.StableEvent

@Immutable
internal data class ProfileListUiState(
    val status: UiStatus = UiStatus.Loading,
    val items: List<Item> = emptyList(),
    val activeId: String = "",
    val error: String = "",
) {
    @Immutable
    sealed interface Item {
        val key: String
        val contentType: String
    }

    enum class ClickEvent {
        Edit,
        Delete,
        Select
    }
}