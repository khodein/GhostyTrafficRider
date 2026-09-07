package com.ghosty.traffic.rider.framework.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

@Immutable
data class AppPaddings(
    private val zero: Dp = 0.dp,
    private val extraSmall: Dp = 4.dp,
    private val small: Dp = 8.dp,
    private val normal: Dp = 12.dp,
    private val medium: Dp = 16.dp,
    private val large: Dp = 24.dp,
) {
    fun extraSmall() = extraSmall
    fun small() = small
    fun normal() = normal
    fun medium() = medium
    fun large() = large

    fun allExtraSmall() = PaddingValues(extraSmall, extraSmall, extraSmall, extraSmall)
    fun allSmall() = PaddingValues(small, small, small, small)
    fun allNormal() = PaddingValues(normal, normal, normal, normal)
    fun allMedium() = PaddingValues(medium, medium, medium, medium)
    fun allLarge() = PaddingValues(large, large, large, large)

    fun horizontalExtraSmall() = PaddingValues(extraSmall, zero, extraSmall, zero)
    fun horizontalSmall() = PaddingValues(small, zero, small, zero)
    fun horizontalNormal() = PaddingValues(normal, zero, normal, zero)
    fun horizontalMedium() = PaddingValues(medium, zero, medium, zero)
    fun horizontalLarge() = PaddingValues(large, zero, large, zero)

    fun verticalExtraSmall() = PaddingValues(zero, extraSmall, zero, extraSmall)
    fun verticalSmall() = PaddingValues(zero, small, zero, small)
    fun verticalNormal() = PaddingValues(zero, normal, zero, normal)
    fun verticalMedium() = PaddingValues(zero, medium, zero, medium)
    fun verticalLarge() = PaddingValues(zero, large, zero, large)
}

internal val LocalAppPaddings = staticCompositionLocalOf { AppPaddings() }
