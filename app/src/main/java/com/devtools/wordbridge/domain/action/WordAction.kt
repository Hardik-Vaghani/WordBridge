package com.devtools.wordbridge.domain.action

import com.devtools.wordbridge.domain.model.Word

sealed class WordAction {
    data class Edit(val word: Word) : WordAction()
//    object NavigateToEdit : WordAction()
    data class Delete(val word: Word) : WordAction()
    data class ToggleFavourite(val wordId: Int, val isFavorite: Boolean) : WordAction()
    data class ExpandCollapse(val wordId: Int) : WordAction()

    companion object {
        fun fromRoute(route: String, word: Word? = null): WordAction? {
            return when (route) {
                "edit" -> word?.let { Edit(it) }
                "delete" -> word?.let { Delete(it) }
                // ... other mappings
                else -> null
            }
        }
    }
}
