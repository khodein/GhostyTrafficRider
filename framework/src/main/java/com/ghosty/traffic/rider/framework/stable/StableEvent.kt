package com.ghosty.traffic.rider.framework.stable

import androidx.compose.runtime.Stable

@Stable
abstract class StableEventBase(
    private val stableKey: Any,
) {

    final override fun equals(other: Any?): Boolean {
        return other is StableEventBase &&
                this::class == other::class &&
                stableKey == other.stableKey
    }

    final override fun hashCode(): Int {
        return 31 * this::class.hashCode() + stableKey.hashCode()
    }
}

@Stable
class StableClickEvent(
    stableKey: Any,
    private val action: () -> Unit,
) : StableEventBase(stableKey), () -> Unit {

    override operator fun invoke() = action()
}

@Stable
class StableEvent<T>(
    stableKey: Any,
    private val action: (T) -> Unit,
) : StableEventBase(stableKey), (T) -> Unit {

    override operator fun invoke(value: T) = action(value)
}
