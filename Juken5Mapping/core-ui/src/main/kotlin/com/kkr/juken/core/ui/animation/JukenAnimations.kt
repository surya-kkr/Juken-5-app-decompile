package com.kkr.juken.core.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object JukenAnimations {
    /**
     * Default spring for UI elements like buttons and cards.
     */
    val GentleSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    /**
     * Standard tween for transitions.
     */
    val StandardTween = tween<Float>(durationMillis = 300)

    /**
     * Fast tween for small state changes.
     */
    val FastTween = tween<Float>(durationMillis = 150)
}
