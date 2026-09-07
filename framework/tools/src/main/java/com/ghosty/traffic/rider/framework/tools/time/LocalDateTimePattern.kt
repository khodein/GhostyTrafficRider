package com.ghosty.traffic.rider.framework.tools.time

sealed interface LocalDateTimePattern {
    val value: String

    data object Short : LocalDateTimePattern { override val value = "dd.MM.yyyy" }
    data object Medium : LocalDateTimePattern { override val value = "dd.MM.yyyy HH:mm" }
    data object Long : LocalDateTimePattern { override val value = "dd.MM.yyyy HH:mm:ss" }
}
