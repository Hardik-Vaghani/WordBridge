package com.devtools.wordbridge.domain.action

import com.devtools.wordbridge.core.action.Action
import com.devtools.wordbridge.domain.model.Word
//
//// UI
//sealed class UiAction<T> : Action {
//    data class Click<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class DoubleClick<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class LongPress<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class SwipeLeft<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class SwipeRight<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class ToggleExpandCollapse<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class NavigateToScreen<T>(val route: String? = null, val data: T? = null) : UiAction<T>()
//}

//
//// API
//sealed class ApiAction : Action {
//    data class GetUser(val userId: Int) : ApiAction()
//    data class PostData(val payload: Any) : ApiAction()
//}
//
//// Database
//sealed class DbAction : Action {
//    data class InsertWord(val word: Word) : DbAction()
//    data class DeleteWord(val word: Word) : DbAction()
//    data class UpdateWord(val word: Word) : DbAction()
//    data class ToggleFavouriteWord(val wordId: Int, val isFavorite: Boolean) : DbAction()
//}

