package com.ghosty.traffic.rider.framework.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppCorners(
    private val small: Dp = 8.dp,
    private val medium: Dp = 16.dp,
    private val large: Dp = 24.dp,
) {
    fun medium() = medium
    fun small() = small
    fun large() = large

    fun top(corner: Dp = medium): RoundedCornerShape = RoundedCornerShape(
        topStart = corner,
        topEnd = corner,
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
    )

    fun bottom(corner: Dp = medium): RoundedCornerShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = corner,
        bottomEnd = corner,
    )

    fun all(corner: Dp = medium): RoundedCornerShape = RoundedCornerShape(corner)
}

internal val LocalAppCorners = staticCompositionLocalOf { AppCorners() }
