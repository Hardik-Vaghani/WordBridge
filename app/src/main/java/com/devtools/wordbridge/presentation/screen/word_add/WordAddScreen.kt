package com.devtools.wordbridge.presentation.screen.word_add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devtools.wordbridge.R
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.ui.custom_ui.MessageAlert
import com.devtools.wordbridge.presentation.ui.theme.ColorError
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderUnselectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.ColorSelected
import com.devtools.wordbridge.presentation.ui.theme.ColorWarning
import kotlinx.coroutines.launch

@Composable
fun WordAddScreen(
    viewModel: WordAddViewModel = hiltViewModel(),
    onWordSaved: () -> Unit = {},
    onBackClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf<String?>(null) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.8f) // 70%
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val word by viewModel.word.collectAsState()
                    val meaning by viewModel.meaning.collectAsState()
                    val translation by viewModel.translation.collectAsState()
                    val pronunciation by viewModel.pronunciation.collectAsState()

                    val focusManager = LocalFocusManager.current

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Add word", color = ColorOutlinedTextBorder, style = MaterialTheme.typography.headlineMedium)
                        Row(modifier = Modifier.fillMaxWidth().height(33.dp), horizontalArrangement = Arrangement.End) {

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                onClick = { onBackClicked()}
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_back),
                                    //                        painter = rememberVectorPainter(image = Icons.Default.Search),
                                    contentDescription = "Back button",
                                    modifier = Modifier
                                        .size(width = 64.dp, height = 32.dp)
                                        .background(color = Color.Transparent)
                                        .border(width = 1.dp, color = ColorIconBorderUnselectedItem, shape = RoundedCornerShape(8.dp))
                                        .padding(4.dp),
                                    colorFilter = ColorFilter.tint(ColorIconBorderUnselectedItem)
                                )
                            }
                        }
                    }

                    var isErrorOfWord by remember { mutableStateOf(false) }
                    var onFocusedOfWord by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = word,
                        onValueChange = {
                            viewModel.onWordChanged(it)
                            isErrorOfWord = it.isBlank()
                        },
                        label = {
                            Text(
                                "Word",
                                color = if (isErrorOfWord) ColorError else ColorOutlinedTextBorder
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            if (word.isBlank()) isErrorOfWord = true
                            else focusManager.moveFocus(FocusDirection.Down)
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { onFocusedOfWord = it.isFocused },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isErrorOfWord) ColorError else ColorOutlinedTextBorder,
                            unfocusedBorderColor = if (isErrorOfWord) ColorError else ColorOutlinedTextBorder.copy(
                                alpha = 0.5f
                            ),
                            cursorColor = if (isErrorOfWord) ColorError else ColorOutlinedTextBorder,
                        )
                    )
                    if (onFocusedOfWord || isErrorOfWord) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "Primary word is required",
                            color = if (isErrorOfWord) ColorError else ColorOutlinedTextBorder,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    var isWarningOfMeaning by remember { mutableStateOf(false) }
                    var onFocusedOfMeaning by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = meaning,
                        onValueChange = {
                            viewModel.onMeaningChanged(it)
                            isWarningOfMeaning = it.isBlank() // keep logic consistent
                        },
                        label = {
                            Text(
                                if (onFocusedOfMeaning) "Meaning" else "Meaning (optional)",
                                color = if (isWarningOfMeaning) ColorWarning else ColorOutlinedTextBorder
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            if (meaning.isBlank()) isWarningOfMeaning = true
                            focusManager.moveFocus(FocusDirection.Down)
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { onFocusedOfMeaning = it.isFocused },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isWarningOfMeaning) ColorWarning else ColorOutlinedTextBorder,
                            unfocusedBorderColor = if (isWarningOfMeaning) ColorWarning else ColorOutlinedTextBorder.copy(
                                alpha = 0.5f
                            ),
                            cursorColor = if (isWarningOfMeaning) ColorWarning else ColorOutlinedTextBorder
                        )
                    )


                    var isErrorOfTranslation by remember { mutableStateOf(false) }
                    var onFocusedOfTranslation by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = translation,
                        onValueChange = {
                            viewModel.onTranslationChanged(it)
                            isErrorOfTranslation = it.isBlank()
                        },
                        label = {
                            Text(
                                "Translation",
                                color = if (isErrorOfTranslation) ColorError else ColorOutlinedTextBorder
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            if (translation.isBlank()) isErrorOfTranslation = true else
                                focusManager.moveFocus(FocusDirection.Down)
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { onFocusedOfTranslation = it.isFocused },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isErrorOfTranslation) ColorError else ColorOutlinedTextBorder,
                            unfocusedBorderColor = if (isErrorOfTranslation) ColorError else ColorOutlinedTextBorder.copy(
                                alpha = 0.5f
                            ),
                            cursorColor = if (isErrorOfTranslation) ColorError else ColorOutlinedTextBorder
                        )
                    )
                    if (onFocusedOfTranslation || isErrorOfTranslation) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "Secondary word (translation) is required",
                            color = if (isErrorOfTranslation) ColorError else ColorOutlinedTextBorder,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    var isWarningOfPronunciation by remember { mutableStateOf(false) }
                    var isFocusedOfPronunciation by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = pronunciation,
                        onValueChange = {
                            viewModel.onPronunciationChanged(it)
                            if (it.isNotBlank()) isWarningOfPronunciation = false
                        },
                        label = {
                            Text(
                                if (isFocusedOfPronunciation) "Pronunciation" else "Pronunciation (optional)",
                                color = if (isWarningOfPronunciation) ColorWarning else ColorOutlinedTextBorder
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            if (pronunciation.isBlank()) isWarningOfPronunciation = true
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isFocusedOfPronunciation = it.isFocused },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isWarningOfPronunciation) ColorWarning else ColorOutlinedTextBorder,
                            unfocusedBorderColor = if (isWarningOfPronunciation) ColorWarning else ColorOutlinedTextBorder.copy(
                                alpha = 0.5f
                            ),
                            cursorColor = if (isWarningOfPronunciation) ColorWarning else ColorOutlinedTextBorder
                        )
                    )

                    Button(
                        onClick = {
                            if(word.isBlank() || translation.isBlank()){ return@Button }
                            viewModel.saveWord { status: OperationStatus<Word>? ->
                                val msgText = when (status) {
                                    is OperationStatus.Success -> status.message
                                    is OperationStatus.Failed -> status.message
                                    is OperationStatus.Ongoing -> status.message
                                    null -> null
                                }

                                onWordSaved() //callback
                                isWarningOfMeaning = false
                                isWarningOfPronunciation = false
                                coroutineScope.launch { alertMessage = msgText }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), // nice tall button
                        shape = RoundedCornerShape(12.dp), // rounded corners
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorOutlinedTextBorder, // background
                            contentColor = ColorSelected, // text/icon color
                            disabledContainerColor = ColorOutlinedTextBorder,
                            disabledContentColor = ColorOutlinedTextBorder
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp,
                            focusedElevation = 4.dp,
                            hoveredElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.titleMedium.copy( // bigger/bolder text
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.2f) // 30%
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MessageAlert(
                    message = alertMessage,
                    onDismiss = { alertMessage = null }
                )
            }
        }
    }
}