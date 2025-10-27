package com.devtools.wordbridge.core.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp

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

fun Rect.toIntRect(): IntRect = IntRect(
    left = this.left.toInt(),
    top = this.top.toInt(),
    right = this.right.toInt(),
    bottom = this.bottom.toInt()
)

@Composable
fun actionBarSizeDp(): Dp {
    val context = LocalContext.current
    val density = LocalDensity.current

    return remember(context, density) {
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            // convert pixel size to Dp
            with(density) {
                TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    context.resources.displayMetrics
                ).toDp()
            }
        } else {
            56.dp // fallback
        }
    }
}
