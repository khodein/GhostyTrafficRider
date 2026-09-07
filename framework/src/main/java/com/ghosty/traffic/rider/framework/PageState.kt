package com.ghosty.traffic.rider.framework

import androidx.compose.runtime.Stable

@Stable
data class PageState<T>(
    val items: List<T>,
    val page: Int = 0,
    val status: UiStatus = UiStatus.Loading,
    val isEndReached: Boolean = false,
)
