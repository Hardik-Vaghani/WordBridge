package com.devtools.wordbridge.presentation.ui.theme

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun ShakeAnimationOld(
    trigger: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            offsetX.snapTo(0f)
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 500
                    -20f at 50
                    20f at 100
                    -15f at 150
                    15f at 200
                    -10f at 250
                    10f at 300
                    -5f at 350
                    5f at 400
                    0f at 500
                }
            )
        }
    }

    content(Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) })
}
@Composable
fun ShakeAnimation(
    trigger: Boolean,
    shakeDistance: Float = 20f, // max horizontal offset
    duration: Int = 500,  // total duration
    content: @Composable (Modifier) -> Unit
) {
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            offsetX.snapTo(0f)
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = duration
                    // simple shake pattern: alternate left/right decreasing
                    (-shakeDistance) at (durationMillis * 50 / 500)
                    (shakeDistance) at (durationMillis * 100 / 500)
                    (-shakeDistance * 0.75f) at (durationMillis * 150 / 500)
                    (shakeDistance * 0.75f) at (durationMillis * 200 / 500)
                    (-shakeDistance * 0.5f) at (durationMillis * 250 / 500)
                    (shakeDistance * 0.5f) at (durationMillis * 300 / 500)
                    (-shakeDistance * 0.25f) at (durationMillis * 350 / 500)
                    (shakeDistance * 0.25f) at (durationMillis * 400 / 500)
                    0f at durationMillis
                }
            )
        }
    }

    content(Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) })
}


@Composable
fun BounceZoomAnimation(
    selected: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(selected) {
        if (selected) {
            // Animate scale
            scale.animateTo(
                targetValue = 1.065f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
            // Animate rotation only once
            rotation.snapTo(0f)
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        } else {
            // Reset scale when unselected
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
            // Keep rotation at 360f so it doesn't reverse
            rotation.snapTo(360f)
        }
    }

    content(
        Modifier.graphicsLayer(
            scaleX = scale.value,
            scaleY = scale.value,
            rotationZ = rotation.value
        )
    )
}

@Composable
fun BounceZoomAnimationBothSide(
    selected: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.065f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (selected) 360f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "rotation"
    )

    content(
        Modifier.graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            rotationZ = rotation
        )
    )
}



@Composable
fun ScaleInScaleOut(
    selected: Boolean,
    loopCount: Int = -1, // -1 for infinite
    duration: Int = 1000,
    content: @Composable (Modifier) -> Unit
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(selected) {
        if (selected) {
            var count = 0
            do {
                // Scale down: 1 -> 0
                scale.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = duration / 2, easing = FastOutSlowInEasing)
                )
                // Scale up: 0 -> 1
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = duration / 2, easing = FastOutSlowInEasing)
                )
                count++
            } while (loopCount == -1 || count < loopCount)
        } else {
            // Reset scale if not selected
            scale.snapTo(1f)
        }
    }

    content(
        Modifier.graphicsLayer(
            scaleX = scale.value,
            scaleY = scale.value
        )
    )
}

/** Animation enter */
val enter_01 = slideInVertically(initialOffsetY = { it }) + fadeIn()
val enter_02 = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium))
val enter_03 = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))
val enter_04 = slideInVertically(initialOffsetY = { it }) + scaleIn( animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))
val enter_05 = scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))
val enter_06 = scaleIn(initialScale = 0.8f) + fadeIn()
val enter_07 = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
val enterBouncy = scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow)) + fadeIn(animationSpec = tween(300))
val enterBouncyHorizontal = (slideInHorizontally(initialOffsetX = { -it / 2 }, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)) + scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300)))

/** Animation exit */
val exit_01 = slideOutVertically(targetOffsetY = { it }) + fadeOut()
val exit_02 = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium))
val exit_03 = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))
val exit_04 = slideOutVertically(targetOffsetY = { it }) + scaleOut( animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))
val exit_05 = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))
val exit_06 = scaleOut(targetScale = 0.8f) + fadeOut()
val exit_07 = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut()
val exitBouncy = scaleOut(targetScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)) + fadeOut(animationSpec = tween(150))

