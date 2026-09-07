package com.ghosty.traffic.rider.framework.tools.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocalDateTimeFormatter(
    private val value: LocalDateTime,
) {
    companion object {
        fun of(timestamp: Long): LocalDateTimeFormatter {
            val localDateTime = Instant
                .ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            return LocalDateTimeFormatter(localDateTime)
        }
    }

    fun toTimestamp(): Long = value
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    fun format(pattern: LocalDateTimePattern): String =
        value.format(DateTimeFormatter.ofPattern(pattern.value))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocalDateTimeFormatter) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()
}
