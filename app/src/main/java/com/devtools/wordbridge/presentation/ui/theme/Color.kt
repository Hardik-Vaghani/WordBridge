package com.devtools.wordbridge.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.devtools.wordbridge.R

// --- Dark theme style (neon accents on dark) ---
val Purple80 = Color(0xFF9B5FFF)      // Neon violet accent
val PurpleGrey80 = Color(0xFF8B92A3)  // Muted slate grey
val Pink80 = Color(0xFFFF7EB6)        // Neon pink highlight

// --- Light theme style (soft pastel on light) ---
val Purple40 = Color(0xFF5E4FD5)      // Deep indigo primary
val PurpleGrey40 = Color(0xFF7A8090)  // Cool grey accent
val Pink40 = Color(0xFFE95B8A)        // Bright rose accent

// --- Shared custom colors ---
@Composable
fun appBackground() = colorResource(R.color.app_base_background)

@Composable
fun appHeaderBackground() = colorResource(R.color.header_background)

@Composable
fun appFooterBackground() = colorResource(R.color.footer_background)
@Composable
fun colorSelected(): Color = colorResource(id = R.color.color_selected)  // White on dark
@Composable fun colorUnselected(): Color = colorResource(id = R.color.color_unselected)
@Composable fun colorMessageAlertBackground(): Color = colorResource(id = R.color.color_message_alert_background)
@Composable fun colorError(): Color = colorResource(id = R.color.color_error)
@Composable fun colorWarning(): Color = colorResource(id = R.color.color_warning)

@Composable fun colorOutlinedTextBorder(): Color = colorResource(id = R.color.color_outlined_text_border)
@Composable fun colorOutlinedTextBorderActive(): Color = colorResource(id = R.color.color_outlined_text_border).copy(alpha = 0.8f)
@Composable fun colorOutlinedTextBorderDeActive(): Color = colorResource(id = R.color.color_outlined_text_border).copy(alpha = 0.5f)

@Composable fun colorIconBorderActivate(): Color = colorResource(id = R.color.color_outlined_text_border).copy(alpha = 0.8f)
@Composable fun colorIconBorderDeactivate(): Color = colorResource(id = R.color.color_outlined_text_border).copy(alpha = 0.5f)

@Composable fun colorDividerSeparator_1(): Color = colorResource(id = R.color.color_unselected)
@Composable fun colorDividerSeparator_2(): Color = colorResource(id = R.color.color_selected)
@Composable fun colorItemBackground(): Color = colorResource(id = R.color.color_item_background)
