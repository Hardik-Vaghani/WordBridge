package com.devtools.wordbridge.domain.action

import com.devtools.wordbridge.core.action.DomainAction
import com.devtools.wordbridge.core.action.UiAction
import com.devtools.wordbridge.domain.model.Word


/** Domain events */
sealed class DomainActions : DomainAction {
    // Database
    data class InsertWord(val word: Word) : DomainAction
    data class DeleteWord(val word: Word) : DomainAction
    data class UpdateWord(val word: Word) : DomainAction
    data class ToggleFavouriteWord(val wordId: Int, val isFavorite: Boolean) : DomainAction

    // API
//    data class GetUser(val userId: Int) : DomainAction
//    data class PostData(val payload: Any) : DomainAction
}
