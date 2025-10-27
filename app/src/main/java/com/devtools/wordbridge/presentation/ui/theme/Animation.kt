package com.devtools.wordbridge.presentation.ui.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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

/**
 * ↖  top-left
 * ↘  bottom-right
 * ↗  top-right
 * ↙  bottom-left
 * (repeat)
 * */
@Composable
fun shake4WayAnimation(
    trigger: Boolean,
    distance: Float = 4f,     // max offset in dp
    duration: Int = 1400,      // full cycle duration
    content: @Composable (Modifier) -> Unit
) : Pair<Float, Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "shake4way")

    // X offset moves: 0 → -distance → +distance → +distance → -distance → loop
    val shakeX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0f at 0
                -distance at duration / 4   // top-left
                +distance at duration / 2   // bottom-right
                +distance at (3 * duration / 4) // top-right
                -distance at duration       // bottom-left
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeX"
    )

    // Y offset moves: 0 → -distance → +distance → -distance → +distance → loop
    val shakeY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0f at 0
                -distance at duration / 4   // top-left
                +distance at duration / 2   // bottom-right
                -distance at (3 * duration / 4) // top-right
                +distance at duration       // bottom-left
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeY"
    )

    val modifier = if (trigger) {
        Modifier.offset(x = shakeX.dp, y = shakeY.dp)
    } else {
        Modifier // no offset if not triggered
    }

    content(modifier)
    return shakeX to shakeY
}

@Composable
fun shakeRandomAnimation(
    trigger: Boolean,
    distance: Float = 4f,
    intervalMs: Long = 1400L,
    content: @Composable (Modifier) -> Unit
) : Pair<Float, Float> {
    val x = remember { Animatable(0f) }
    val y = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            while (true) {
                val newX = if ((0..1).random() == 0) -distance else distance
                val newY = if ((0..1).random() == 0) -distance else distance
                x.animateTo(newX, tween(intervalMs.toInt()))
                y.animateTo(newY, tween(intervalMs.toInt()))
                // then swap to the opposite direction
            }
        } else {
            x.snapTo(0f); y.snapTo(0f)
        }
    }

    val modifier = Modifier.offset(x = x.value.dp, y = y.value.dp)
    content(modifier)
    return x.value to y.value
}
enum class ShakeDirection {
    Horizontal,
    Vertical
}
@Composable
fun shake2WayAnimation(
    trigger: Boolean,
    initialValue: Float = -4f,     // max offset in dp
    targetValue: Float = 4f,     // max offset in dp
    duration: Int = 400,      // full cycle duration
    direction: ShakeDirection = ShakeDirection.Horizontal,
    content: @Composable (Modifier) -> Unit
) : Float {

    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )
    val modifier = if (trigger) {
        when (direction) {
            ShakeDirection.Horizontal -> Modifier.offset(x = shakeOffset.dp)
            ShakeDirection.Vertical -> Modifier.offset(y = shakeOffset.dp)
        }
    } else {
        Modifier
    }
    content(modifier)

    return shakeOffset
}
/**
 * ENTER ANIMATIONS
 */

// Slide Animations
val enterSlideUp = slideInVertically(initialOffsetY = { it }) + fadeIn()
val enterSlideDown = slideInVertically { fullHeight -> -fullHeight } + fadeIn()
val enterSlideLeft = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
val enterSlideRight = slideInHorizontally { fullWidth -> -fullWidth } + fadeIn()

// Scale Animations
val enterScaleSimple = scaleIn() + fadeIn()
val enterScaleNoBounce = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium))
val enterScaleMediumBounce = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))
val enterScaleInitialSmall = scaleIn(initialScale = 0.8f) + fadeIn()
val enterScaleLowBounce = scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))

// Expand Animations
val enterExpandVertical = expandVertically() + fadeIn()

// Combined Slide + Scale Animations
val enterSlideUpScale = slideInVertically(initialOffsetY = { it }) + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))

val enterSlideDownHalfScale = slideInVertically { fullHeight -> -fullHeight / 2 } + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))

val enterSlideUpFullScale = slideInVertically { fullHeight -> fullHeight } + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))

val enterSlideUpThirdScale = slideInVertically { fullHeight -> fullHeight / 3 } + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium)) + fadeIn(animationSpec = tween(300))

val enterSlideUpQuarterScale = slideInVertically { fullHeight -> fullHeight / 4 } + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))


// Special Bouncy Animations
val enterSlideDownBouncy = slideInVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) { fullHeight -> -fullHeight / 2 } + fadeIn(animationSpec = tween(300))

val enterSlideUpBouncy = slideInVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) { fullHeight -> fullHeight } + fadeIn(animationSpec = tween(300))

val enterBouncyVeryLow = scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow)) + fadeIn(animationSpec = tween(300))

val enterBouncyHorizontal = slideInHorizontally(initialOffsetX = { -it / 2 }, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)) + scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(animationSpec = tween(300))

/**
 * EXIT ANIMATIONS
 */

// Slide Animations
val exitSlideDown = slideOutVertically(targetOffsetY = { it }) + fadeOut()
val exitSlideUp = slideOutVertically { fullHeight -> -fullHeight } + fadeOut()
val exitSlideRight = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut()
val exitSlideLeft = slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()

// Scale Animations
val exitScaleSimple = scaleOut() + fadeOut()
val exitScaleNoBounce = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium))
val exitScaleMediumBounce = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))
val exitScaleTargetSmall = scaleOut(targetScale = 0.8f) + fadeOut()
val exitScaleNoBounceSmall = scaleOut(targetScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)) + fadeOut(animationSpec = tween(150))

// Shrink Animations
val exitShrinkVertical = shrinkVertically() + fadeOut()

// Combined Slide + Scale Animations
val exitSlideDownScale = slideOutVertically(targetOffsetY = { it }) + scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))

val exitSlideDownHalfScale = slideOutVertically { fullHeight -> -fullHeight / 2 } + scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))

val exitSlideUpFullScale = slideOutVertically { fullHeight -> fullHeight } + scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))

val exitSlideUpThirdScale = slideOutVertically { fullHeight -> fullHeight / 3 } + scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium)) + fadeOut(animationSpec = tween(300))

val exitSlideUpQuarterScale = slideOutVertically { fullHeight -> fullHeight / 4 } + scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeOut(animationSpec = tween(300))

// Special Bouncy Animations
val exitSlideDownBouncy = slideOutVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)) { fullHeight -> -fullHeight / 2 } + fadeOut(animationSpec = tween(150))
val exitSlideUpBouncy = slideOutVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)) { fullHeight -> fullHeight } + fadeOut(animationSpec = tween(150))

// Special Exit Variations
val exitScaleDownThreshold = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow, visibilityThreshold = 0.01f)) + fadeOut(animationSpec = tween(300))

/**
 * ANIMATION PAIRS (Enter + Exit combinations)
 */

// Mirror Pairs (Enter and Exit are symmetrical)
val animationPairSlideUpDown = enterSlideUp to exitSlideDown
val animationPairSlideDownUp = enterSlideDown to exitSlideUp
val animationPairSlideLeftRight = enterSlideLeft to exitSlideRight
val animationPairSlideRightLeft = enterSlideRight to exitSlideLeft
val animationPairSlideRightRight = enterSlideRight to exitSlideRight
val animationPairSlideLeftLeft = enterSlideLeft to exitSlideLeft

val animationPairScaleSimple = enterScaleSimple to exitScaleSimple
val animationPairScaleNoBounce = enterScaleNoBounce to exitScaleNoBounce
val animationPairScaleMediumBounce = enterScaleMediumBounce to exitScaleMediumBounce
val animationPairScaleInitialSmall = enterScaleInitialSmall to exitScaleTargetSmall

val animationPairExpandShrink = enterExpandVertical to exitShrinkVertical

// Combined Slide + Scale Pairs
val animationPairSlideUpScale = enterSlideUpScale to exitSlideDownScale
val animationPairSlideDownHalfScale = enterSlideDownHalfScale to exitSlideDownHalfScale
val animationPairSlideUpFullScale = enterSlideUpFullScale to exitSlideUpFullScale
val animationPairSlideUpThirdScale = enterSlideUpThirdScale to exitSlideUpThirdScale
val animationPairSlideUpQuarterScale = enterSlideUpQuarterScale to exitSlideUpQuarterScale

// Special Bouncy Pairs
val animationPairBouncyVeryLow = enterBouncyVeryLow to exitScaleNoBounceSmall
val animationPairBouncyHorizontal = enterBouncyHorizontal to exitSlideLeft

// Contrast Pairs (Enter and Exit in different directions for dynamic effect)
val animationPairSlideUpSlideLeft = enterSlideUp to exitSlideLeft
val animationPairSlideDownSlideRight = enterSlideDown to exitSlideRight
val animationPairScaleSlideLeft = enterScaleMediumBounce to exitSlideLeft
val animationPairBouncyScaleDown = enterBouncyVeryLow to exitScaleDownThreshold
val animationPairBouncyUpScale = enterSlideDownBouncy to exitShrinkVertical
val animationPairBouncyScale = enterScaleMediumBounce to exitShrinkVertical
val animationPairBouncySlideMenu = enterScaleMediumBounce to exitScaleMediumBounce


/**
 * QUICK ACCESS PRESETS
 */

// Quick presets for common use cases
val presetQuickFade = enterScaleSimple to exitScaleSimple
val presetSlideUp = enterSlideUp to exitSlideDown
val presetBouncyScale = enterScaleMediumBounce to exitScaleMediumBounce
val presetSmoothExpand = enterExpandVertical to exitShrinkVertical
val presetDynamicContrast = enterSlideUp to exitSlideLeft

// Dropdown specific presets
val presetDropdownBouncy = enterSlideUpQuarterScale to exitSlideUpQuarterScale
val presetDropdownSmooth = enterSlideUpThirdScale to exitSlideUpThirdScale
val presetDropdownQuick = enterSlideDownHalfScale to exitSlideDownHalfScale