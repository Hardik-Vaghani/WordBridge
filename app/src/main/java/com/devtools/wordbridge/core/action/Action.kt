package com.devtools.wordbridge.core.action

interface Action          // marker (nothing else)
interface UiAction : Action
interface DomainAction : Action   // anything DB/API/Business

//extra marker interfaces unless you have more complex action hierarchies.
interface UiActionMarker : UiAction
interface DomainActionMarker : DomainAction

