package com.devtools.wordbridge.presentation.ui.custom_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.devtools.wordbridge.domain.model.Word
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.devtools.wordbridge.R
import com.devtools.wordbridge.domain.action.WordAction
import com.devtools.wordbridge.presentation.ui.theme.ColorDividerSeparator_1
import com.devtools.wordbridge.presentation.ui.theme.ColorItemBackground
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderDeActive

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(dismissDirection: SwipeToDismissBoxValue) {
    val color = when (dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color.Blue.copy(alpha = 0.5f)
        SwipeToDismissBoxValue.EndToStart -> Color.Green.copy(alpha = 0.5f)
        else -> Color.Transparent
    }
    val alignment = when (dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .background(color = ColorOutlinedTextBorder.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
            .border(
                BorderStroke(0.1.dp, color = color), // ring thickness & color
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = alignment
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = when (dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> "Unfav"
                SwipeToDismissBoxValue.EndToStart -> "Fav"
                else -> ""
            },
            color = ColorOutlinedTextBorderDeActive
        )
    }
}

@Composable
private fun WordCard(
    word: Word,
    isExpanded: Boolean,
    onSingleClick: () -> Unit,
    onDoubleClick: () -> Unit,
    onLongPress: () -> Unit,
    onToggleFavourite: (Int, Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .align(Alignment.Center)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .align(Alignment.Center)   // center of box
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onSingleClick() },
                            onDoubleTap = { onDoubleClick() },
                            onLongPress = { onLongPress() }
                        )
                    },
                colors = CardDefaults.cardColors(containerColor = ColorItemBackground)
            ) {
                val horizontalArrangement = Arrangement.SpaceBetween
                Column(Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = horizontalArrangement
                    ) {
                        Text(
                            text = "Primary: ${word.primaryWord}",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Text(
                            text = "Secondary: ${word.secondaryWord}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    // Only visible when expanded
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(Modifier
                            .fillMaxWidth()
                        ) {

                            DottedHorizontalDivider(
                                modifier = Modifier.height(18.dp).padding(horizontal = 0.dp),
                                color = ColorDividerSeparator_1.copy(alpha = 0.5f),
                                thickness = 0.7.dp,
                                dashLength = 6f,
                                gapLength = 6f
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = horizontalArrangement
                            ) {
                                Text(
                                    "Meaning: ${word.wordMeaning}",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    "Pronunciation: ${word.secondaryWordPronunciation}",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd) // ⬅️ key line
                .offset(x = (16).dp, y = (0).dp)
                .background(Color.Transparent, shape = CircleShape)
                .size(32.dp)
                .clip(CircleShape)
                .clickable (
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = {onToggleFavourite(word.id, !word.isFavorite)},
            )
        ){
            Image(
//                        painter = rememberVectorPainter(image = Icons.Default.Search),
                painter = painterResource(
                    if (word.isFavorite) R.drawable.ic_selected_favorite
                    else R.drawable.ic_unselected_favorite,

                ),
                contentDescription = "Favorite button",
                modifier = Modifier
                    .size(width = 64.dp, height = 32.dp)
                    .background(color = Color.Transparent)
                    .padding(4.dp),
//                        .border(width = 1.dp, color = ColorOutlinedTextBorderDeActive, shape = RoundedCornerShape(8.dp))
                    colorFilter = ColorFilter.tint(
                        if (word.isFavorite) Color.Yellow.copy(alpha = 1f) else Color.Transparent.copy(alpha = 0.0f))
            )
        }

    }
}

@Composable
private fun WordActionsDialog(
    show: Boolean,
    word: Word,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFav: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {},
            text = {
                Column {
                    TextButton(onClick = { onEdit(); onDismiss() }) { Text("Edit") }
                    TextButton(onClick = { onDelete(); onDismiss() }) { Text("Delete") }
                    TextButton(onClick = { onToggleFav(); onDismiss() }) { Text("Favourite / Unfavourite") }
                    TextButton(onClick = onDismiss) { Text("Close") }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordItemRow(
    word: Word,
    isExpanded: Boolean,
    onAction: (WordAction) -> Unit,
    enableSwipeStartToEnd: Boolean = true,
    enableSwipeEndToStart: Boolean = true
) {
    var showDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState()

    // one Boolean per row, initialised from word.isFavorite
    var favState by remember(word.id) { mutableStateOf(word.isFavorite) }

    // swipe observer
    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> if (enableSwipeEndToStart) {
                val newValue = !favState
                favState = newValue
                onAction(WordAction.ToggleFavourite(word.id, newValue))
                dismissState.reset()
            }
            SwipeToDismissBoxValue.StartToEnd -> if (enableSwipeStartToEnd) {
                val newValue = !favState
                favState = newValue
                onAction(WordAction.ToggleFavourite(word.id, newValue))
                dismissState.reset()
            }
            else -> Unit
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = enableSwipeStartToEnd,
        enableDismissFromEndToStart = enableSwipeEndToStart,
        backgroundContent = { SwipeBackground(dismissState.dismissDirection) },
        content = {
            WordCard(
                word = word.copy(isFavorite = favState), // show local state
                isExpanded = isExpanded,
                onSingleClick = {
                    // Toggle expand/collapse via parent
                    onAction(WordAction.ExpandCollapse(word.id))
                },
                onDoubleClick = {
                    val newValue = !favState
                    favState = newValue
                    onAction(WordAction.ToggleFavourite(word.id, newValue))
                },
                onLongPress = { showDialog = true },
                onToggleFavourite = { id, fav ->
                    favState = fav
                    onAction(WordAction.ToggleFavourite(id, fav))
                }
            )
        }
    )

    WordActionsDialog(
        show = showDialog,
        word = word.copy(isFavorite = favState),
        onEdit = { onAction(WordAction.Edit(word)) },
        onDelete = { onAction(WordAction.Delete(word)) },
        onToggleFav = {
            val newValue = !favState
            favState = newValue
            onAction(WordAction.ToggleFavourite(word.id, newValue))
        },
        onDismiss = { showDialog = false }
    )
}
