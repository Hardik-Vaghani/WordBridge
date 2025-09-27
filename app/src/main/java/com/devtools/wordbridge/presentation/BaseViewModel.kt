package com.devtools.wordbridge.presentation

import androidx.lifecycle.ViewModel
import com.devtools.wordbridge.core.action.Action
import com.devtools.wordbridge.core.action.ActionHandler
import com.devtools.wordbridge.core.action.DomainAction
import com.devtools.wordbridge.core.action.UiAction
import com.devtools.wordbridge.domain.action.DomainActions
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.action.UiActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * BaseViewModel handles both UI events and Domain actions.
 * Subclasses override methods to implement specific behaviour.
 */
@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel(), ActionHandler<Action> {

    private val _itemSelection = MutableStateFlow<List<Word>>(emptyList())
    val itemSelection = _itemSelection.asStateFlow()

    // derived property: true if anything selected
    val isSelectionActive: Boolean
        get() = _itemSelection.value.isNotEmpty()

    private fun select(word: Word) {
        val current = _itemSelection.value.toMutableList()
        if (current.any { it.id == word.id }) {
            // already selected → deselect
            current.removeAll { it.id == word.id }
        } else {
            // not selected → select
            current.add(word)
        }
        _itemSelection.value = current
    }

    fun clearSelection() {
        _itemSelection.value = emptyList()
    }


    // ✅ These can be overridden by subclasses
    open fun expandCollapse(wordId: Int) {}
    open fun navigateToUpdateScreen(wordId: Int) {}
    open fun onToggleFavourite(wordId: Int, isFav: Boolean) {}
    open fun upsertWord(word: Word, status: (OperationStatus<Word>?) -> Unit = {}) {}
    open fun deleteWord(word: Word, status: (OperationStatus<Word>?) -> Unit = {}) {}

    /**
     * Entry point for all actions
     */
    override fun onAction(action: Action) {
        when (action) {
            is UiAction -> handleUiAction(action)
            is DomainAction -> handleDomainAction(action)
        }
    }


    open fun handleUiAction(action: UiAction) {
        when(action) {
            is UiActions.Click -> {
                when (val data = action.payload) {
                    is Word -> {
                        if (isSelectionActive) {
                            // selection mode active → toggle selection
                            select(data)
                        } else {
                            // normal click behaviour when no selection
                            expandCollapse(data.id)
                        }
                    }
                }
            }
            is UiActions.DoubleClick -> {
                val word = action.payload as? Word
                word?.let { handleDomainAction(DomainActions.ToggleFavouriteWord(it.id, !it.isFavorite)) }
            }
            is UiActions.LongPress -> {
                when (val data = action.payload) {
                    is Word -> {
                        // long press always toggles selection; if none selected it starts selection mode
                        select(data)
                    }
                }
            }
            is UiActions.ToggleExpandCollapse -> {
                val word = action.payload as? Word
                word?.id?.let { expandCollapse(it) }
            }
            is UiActions.NavigateToScreen -> {
                val word = action.payload as? Word
                word?.id?.let { navigateToUpdateScreen(it) }
            }
            is UiActions.SwipeLeft -> {
                /*val word = action.data as? Word
                val newValue = !word?.isFavorite!!
                Log.d("UI", "SwipeLeft $newValue")*/
            }
            is UiActions.SwipeRight -> {
                /*val word = action.data as? Word
                val newValue = !word?.isFavorite!!
                Log.d("UI", "SwipeRight $newValue")*/
            }
            //else -> Log.d("UI", "Unhandled UI action: $action")
        }
    }

    open fun handleDomainAction(action: DomainAction) {
        when(action) {
            is DomainActions.ToggleFavouriteWord -> onToggleFavourite(action.wordId, action.isFavorite)
            is DomainActions.InsertWord -> upsertWord(action.word)
            is DomainActions.UpdateWord -> upsertWord(action.word)//navigateToUpdateScreen(action.word.id)
            is DomainActions.DeleteWord -> deleteWord(action.word)
        }
    }

    open fun handleApiAction(action: DomainAction) {
        /*when(action) {
            is ApiAction.GetUser -> getUser(action.userId)
            is ApiAction.PostData -> postData(action.payload)
        }*/
    }
}
