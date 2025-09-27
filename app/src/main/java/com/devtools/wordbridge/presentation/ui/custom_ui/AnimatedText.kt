package com.devtools.wordbridge.presentation.ui.custom_ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.devtools.wordbridge.presentation.ui.theme.animationPairSlideLeftLeft
import com.devtools.wordbridge.presentation.ui.theme.animationPairSlideLeftRight
import com.devtools.wordbridge.presentation.ui.theme.animationPairSlideRightLeft
import com.devtools.wordbridge.presentation.ui.theme.animationPairSlideRightRight

@Composable
fun AnimatedTextField(
    text: String,
    visible: Boolean,
    enter: EnterTransition = animationPairSlideRightLeft.first,
    exit: ExitTransition = animationPairSlideRightLeft.second,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleSmall
) {
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = style
        )
    }
}
