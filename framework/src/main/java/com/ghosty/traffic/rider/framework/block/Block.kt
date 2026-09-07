package com.ghosty.traffic.rider.framework.block

import com.ghosty.traffic.rider.framework.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class Block<State : Any, Provider> {

    private var onEvent: ((UiEvent) -> Unit)? = null
    protected var blockScope: CoroutineScope? = null
        private set

    protected var blockProvider: Provider? = null
        private set

    protected abstract fun getInitialUiState(): State

    private val _blockState by lazy { MutableStateFlow(getInitialUiState()) }
    val blockState: StateFlow<State>
        get() = _blockState.asStateFlow()

    internal fun attach(
        scope: CoroutineScope,
        provider: Provider,
        onEvent: (UiEvent) -> Unit,
    ) {
        this.blockScope = scope
        this.blockProvider = provider
        this.onEvent = onEvent
    }

    protected fun updateState(reducer: (State) -> State) {
        val state = blockState.value
        _blockState.update { reducer.invoke(state) }
    }

    protected fun onEvent(event: UiEvent) {
        onEvent?.invoke(event)
    }

    /** Будет вызван один раз, и гарантирует что [blockScope] и [blockProvider] будут доступны*/
    open fun onStartBlock() = Unit

    open fun onUiStart() = Unit

    open fun onUiStop() = Unit
}
