package com.ghosty.traffic.rider.framework.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// region Primitives

private val Orange50 = Color(0xFFFFF3E0)
private val Orange100 = Color(0xFFFFE0B2)
private val Orange200 = Color(0xFFFFCC85)
private val Orange300 = Color(0xFFFFB85C)
private val Orange400 = Color(0xFFFFA333)
private val Orange500 = Color(0xFFFF8C00)
private val Orange600 = Color(0xFFE08200)
private val Orange700 = Color(0xFFC47100)
private val Orange800 = Color(0xFFA35C00)
private val Orange900 = Color(0xFF7A4300)

private val Neutral950 = Color(0xFF0D0D0D)
private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral800 = Color(0xFF2C2C2C)
private val Neutral700 = Color(0xFF3D3D3D)
private val Neutral600 = Color(0xFF525252)
private val Neutral500 = Color(0xFF737373)
private val Neutral400 = Color(0xFFA3A3A3)
private val Neutral300 = Color(0xFFD4D4D4)
private val Neutral200 = Color(0xFFE5E5E5)
private val Neutral100 = Color(0xFFF5F5F5)
private val Neutral50 = Color(0xFFFAFAFA)
private val White = Color(0xFFFFFFFF)

private val Red400 = Color(0xFFF87171)
private val Red500 = Color(0xFFEF4444)

// endregion

// region AppColors

@Stable
interface AppColors {
    // кнопки, switch, активные иконки, акцентные элементы
    val primary: Color
    // текст и иконки поверх primary
    val onPrimary: Color
    // мягкий фон для акцентных блоков, chips, тегов
    val primaryContainer: Color
    // текст и иконки поверх primaryContainer
    val onPrimaryContainer: Color
    // фон всего экрана
    val background: Color
    // основной текст и иконки на фоне background
    val onBackground: Color
    // фон карточек, диалогов, bottom sheet
    val surface: Color
    // фон второстепенных блоков, полей ввода, строк списка
    val surfaceVariant: Color
    // основной текст и иконки на surface
    val onSurface: Color
    // второстепенный текст, подписи, placeholder на surfaceVariant
    val onSurfaceVariant: Color
    // разделители, обводки активных полей ввода
    val outline: Color
    // тонкие разделители, обводки неактивных карточек и полей
    val outlineVariant: Color
    // фон элементов ошибки, деструктивные действия
    val error: Color
    // текст и иконки поверх error
    val onError: Color
}

private class LightAppColors : AppColors {
    override val primary: Color by lazy { Orange600 }
    override val onPrimary: Color by lazy { White }
    override val primaryContainer: Color by lazy { Orange100 }
    override val onPrimaryContainer: Color by lazy { Orange900 }
    override val background: Color by lazy { Neutral100 }
    override val onBackground: Color by lazy { Neutral900 }
    override val surface: Color by lazy { White }
    override val surfaceVariant: Color by lazy { Neutral200 }
    override val onSurface: Color by lazy { Neutral900 }
    override val onSurfaceVariant: Color by lazy { Neutral600 }
    override val outline: Color by lazy { Neutral300 }
    override val outlineVariant: Color by lazy { Neutral200 }
    override val error: Color by lazy { Red500 }
    override val onError: Color by lazy { White }
}

private class DarkAppColors : AppColors {
    override val primary: Color by lazy { Orange500 }
    override val onPrimary: Color by lazy { Neutral950 }
    override val primaryContainer: Color by lazy { Orange900 }
    override val onPrimaryContainer: Color by lazy { Orange100 }
    override val background: Color by lazy { Neutral950 }
    override val onBackground: Color by lazy { Neutral100 }
    override val surface: Color by lazy { Neutral900 }
    override val surfaceVariant: Color by lazy { Neutral800 }
    override val onSurface: Color by lazy { Neutral100 }
    override val onSurfaceVariant: Color by lazy { Neutral400 }
    override val outline: Color by lazy { Neutral700 }
    override val outlineVariant: Color by lazy { Neutral800 }
    override val error: Color by lazy { Red400 }
    override val onError: Color by lazy { Neutral950 }
}

// endregion

internal fun lightAppColors(): AppColors = LightAppColors()
internal fun darkAppColors(): AppColors = DarkAppColors()

internal val LocalAppColors = compositionLocalOf<AppColors> { lightAppColors() }
