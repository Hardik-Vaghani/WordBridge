package com.devtools.wordbridge.core.action

interface ActionHandler<A : Action> {
    fun onAction(action: A)
}