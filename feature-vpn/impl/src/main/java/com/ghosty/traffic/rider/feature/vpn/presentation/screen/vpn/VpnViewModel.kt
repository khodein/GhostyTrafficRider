package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn

import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.VpnMainBlock
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.state.VpnState
import com.ghosty.traffic.rider.framework.BaseViewModel

internal class VpnViewModel(
    private val mainBlock: VpnMainBlock,
) : BaseViewModel<VpnState>() {
    init {
        registerBlocks { add(mainBlock) }
    }

    override fun getInitialUiState() = getState()

    override fun updateViewState() {
        setState { getState() }
    }

    fun start() {
        mainBlock.start()
    }

    private fun getState() = VpnState(main = mainBlock.blockState.value)
}
