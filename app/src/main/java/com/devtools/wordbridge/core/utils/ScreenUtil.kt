package com.devtools.wordbridge.core.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * Calculates the screen width in density-independent pixels (dp).
 *
 * This function can be used outside of Jetpack Compose to obtain the
 * device screen width for layout calculations or responsive UI logic.
 *
 * @param context The Context used to access resources and display metrics.
 * @return The screen width in dp as a Float.
 *
 * @sample
 * val screenWidthDp = getScreenWidthDp(context)
 * val surfaceWidthDp = screenWidthDp * 0.75f // 3/4 of screen width
 *
 * @sample
 * val configuration = LocalConfiguration.current
 * val screenWidth = configuration.screenWidthDp.dp
 * val surfaceWidth = screenWidth * 0.75f // 3/4 of screen width
 *
 * @note
 * use in direct compose fun
 */
fun getScreenWidthDp(context: Context): Float {
    val metrics: DisplayMetrics = context.resources.displayMetrics
    val screenWidthPx = metrics.widthPixels
    val density = metrics.density
    return screenWidthPx / density
}

