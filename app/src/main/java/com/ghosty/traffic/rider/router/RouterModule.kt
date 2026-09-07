package com.ghosty.traffic.rider.router

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import com.ghosty.traffic.rider.framework.router.NavTransition
import com.ghosty.traffic.rider.framework.router.Router
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal data object RouterModule {

    fun get() = module {
        singleOf(::RouterImpl) bind Router::class
    }

    fun enterContentTransform(navTransition: Any?): ContentTransform = when (navTransition) {
        NavTransition.NONE -> EnterTransition.None togetherWith ExitTransition.None
        NavTransition.SLIDE_VERTICAL -> slideInVertically { it } togetherWith fadeOut()
        NavTransition.SLIDE_HORIZONTAL -> ContentTransform(
            targetContentEnter = slideInHorizontally { it },
            initialContentExit = ExitTransition.None,
            targetContentZIndex = 1f,
        )

        else -> fadeIn() togetherWith fadeOut()
    }

    fun popContentTransform(navTransition: Any?): ContentTransform = when (navTransition) {
        NavTransition.NONE -> EnterTransition.None togetherWith ExitTransition.None
        NavTransition.SLIDE_VERTICAL -> fadeIn() togetherWith slideOutVertically { it }
        NavTransition.SLIDE_HORIZONTAL -> ContentTransform(
            targetContentEnter = EnterTransition.None,
            initialContentExit = slideOutHorizontally(
                animationSpec = tween(durationMillis = 300, easing = LinearEasing),
            ) { it },
            targetContentZIndex = -1f,
        )

        else -> fadeIn() togetherWith fadeOut()
    }
}
