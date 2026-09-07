package com.ghosty.traffic.rider.framework

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosty.traffic.rider.framework.block.BlockStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState> : ViewModel() {

    private val blockStore = BlockStore(
        scope = viewModelScope,
        onEvent = ::onEvent
    )

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _viewState by lazy { MutableStateFlow(getInitialUiState()) }
    val viewState: StateFlow<State>
        get() = _viewState

    protected abstract fun getInitialUiState(): State

    protected fun registerBlocks(builder: BlockStore.() -> Unit) {
        if (blockStore.isRegister) return

        blockStore.apply(builder)
        val blocks = blockStore.build()
        if (blocks.isEmpty()) return
        onStartBlocks()

        viewModelScope.launch {
            val flows = blocks.map { it.blockState }
            combine(flows) { }.collectLatest {
                updateViewState()
            }
        }
    }

    protected fun setState(reducer: State.() -> State) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    protected abstract fun updateViewState()

    protected fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    abstract fun attach()

    @CallSuper
    open fun onUiStart() {
        blockStore.onUiStart()
    }

    @CallSuper
    open fun onUiStop() {
        blockStore.onUiStop()
    }

    @CallSuper
    open fun onStartBlocks() {
        blockStore.startBlock()
    }
}
