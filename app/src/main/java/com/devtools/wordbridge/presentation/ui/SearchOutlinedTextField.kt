package com.devtools.wordbridge.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.* // or material.* depending on your setup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(
    showBackground = true, name = "SearchOutlinedTextFieldPreview", group = "CustomView",
    device = "spec:width=411dp,height=891dp,isRound=true", showSystemUi = false,
    wallpaper = Wallpapers.NONE
)
@Composable
fun SearchOutlinedTextFieldPreview() {
    SearchOutlinedTextField(
        searchQuery = "Radha",
        onSearchQueryChange = {},
        searchViewColor = Color.Red,
        modifier = Modifier
    )
}

@Composable
fun SearchOutlinedTextField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchViewColor: Color,
    modifier: Modifier = Modifier
) {
    // 1) Interaction source to observe focus
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor = if (isFocused) searchViewColor else searchViewColor.copy(alpha = 0.7f)
    val shape = RoundedCornerShape(13.dp)

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Search", color = searchViewColor) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(shape) // ensure rounded corners
            .border(width = 2.dp, color = borderColor, shape = shape),
        singleLine = true,
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear text")
                }
            }
        },
        shape = shape,
        interactionSource = interactionSource, // important!
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,   // hide default border
            unfocusedBorderColor = Color.Transparent, // hide default border
            cursorColor = searchViewColor,
            focusedLabelColor = searchViewColor,
            unfocusedLabelColor = searchViewColor.copy(alpha = 0.7f)
        )
    )
}
