package com.ghosty.traffic.rider.framework.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

object AppTheme {

    val color: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val themeController: AppThemeController
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeController.current

    val corner: AppCorners
        @Composable
        @ReadOnlyComposable
        get() = LocalAppCorners.current

    val padding: AppPaddings
        @Composable
        @ReadOnlyComposable
        get() = LocalAppPaddings.current
}

@Composable
fun AppTheme(
    initialTheme: Theme = Theme.SYSTEM,
    content: @Composable () -> Unit
) {
    val controller = remember { appThemeController(initialTheme) }
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (controller.current) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM -> systemDark
    }
    val colors = if (darkTheme) darkAppColors() else lightAppColors()

    CompositionLocalProvider(
        LocalAppThemeController provides controller,
        LocalAppColors provides colors,
        LocalAppTypography provides AppTypography(),
        LocalAppCorners provides AppCorners(),
        LocalAppPaddings provides AppPaddings(),
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(darkTheme),
            content = content
        )
    }
}

private fun AppColors.toMaterialColorScheme(darkTheme: Boolean) = if (darkTheme) {
    darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        surfaceVariant = surfaceVariant,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError
    )
} else {
    lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        surfaceVariant = surfaceVariant,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError
    )
}

enum class Theme { LIGHT, DARK, SYSTEM }

interface AppThemeController {
    val current: Theme
    fun setTheme(theme: Theme)
}

@Stable
private class AppThemeControllerImpl(initial: Theme) : AppThemeController {
    private var _current by mutableStateOf(initial)
    override val current: Theme get() = _current
    override fun setTheme(theme: Theme) {
        _current = theme
    }
}

internal fun appThemeController(initial: Theme = Theme.SYSTEM): AppThemeController =
    AppThemeControllerImpl(initial)

internal val LocalAppThemeController = compositionLocalOf {
    appThemeController()
}
