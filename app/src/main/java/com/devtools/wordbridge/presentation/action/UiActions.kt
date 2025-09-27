package com.devtools.wordbridge.presentation.action

import com.devtools.wordbridge.core.action.Action
import com.devtools.wordbridge.core.action.UiAction

// UI events
sealed class UiActions : UiAction {
    data class Click(val viewId: Int? = null, val payload: Any? = null) : UiActions()
    data class DoubleClick(val viewId: Int? = null, val payload: Any? = null) : UiActions()
    data class LongPress(val viewId: Int? = null, val payload: Any? = null) : UiActions()
    data class SwipeLeft(val payload: Any? = null) : UiActions()
    data class SwipeRight(val payload: Any? = null) : UiActions()
    data class ToggleExpandCollapse(val viewId: Int? = null, val payload: Any? = null) : UiActions()
    data class NavigateToScreen(val route: String? = null, val payload: Any? = null) : UiActions()
}

//sealed class UiAction<T> : Action {
//    data class Click<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>() //data / payload
//    data class DoubleClick<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class LongPress<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class SwipeLeft<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class SwipeRight<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class ToggleExpandCollapse<T>(val viewId: Int? = null, val data: T? = null) : UiAction<T>()
//    data class NavigateToScreen<T>(val route: String? = null, val data: T? = null) : UiAction<T>()
//}

