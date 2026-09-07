package com.ghosty.traffic.rider.framework.router

const val NAV_TRANSITION_KEY = "nav_transition"

enum class NavTransition {
    SLIDE_HORIZONTAL,
    SLIDE_VERTICAL,
    FADE,
    NONE,
}

fun navTransitionMetadata(transition: NavTransition): Map<String, Any> =
    mapOf(NAV_TRANSITION_KEY to transition)
