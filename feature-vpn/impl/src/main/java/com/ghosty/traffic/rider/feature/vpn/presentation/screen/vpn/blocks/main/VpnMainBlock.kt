package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetActiveProfileFlowUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.LoadProfileUseCase
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnStatus
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.GetVpnStatusFlowUseCase
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.StartVpnUseCase
import com.ghosty.traffic.rider.feature.vpn.domain.usecase.StopVpnUseCase
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.RequestVpnPermissionUiEvent
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.mapper.VpnMainBlockMapper
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.state.VpnMainUiState
import com.ghosty.traffic.rider.framework.block.Block
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class VpnMainBlock(
    private val getVpnStatusFlowUseCase: GetVpnStatusFlowUseCase,
    private val getActiveProfileFlowUseCase: GetActiveProfileFlowUseCase,
    private val loadProfileUseCase: LoadProfileUseCase,
    private val startVpnUseCase: StartVpnUseCase,
    private val stopVpnUseCase: StopVpnUseCase,
    private val mapper: VpnMainBlockMapper,
) : Block<VpnMainUiState, Unit>() {
    private var activeProfile: ProfileModel? = null
    private var status = VpnStatus.Disconnected
    private var profileError: String? = null

    override fun getInitialUiState() = mapState()

    override fun onStartBlock() {
        blockScope?.launch {
            runCatching { loadProfileUseCase() }
                .onFailure {
                    profileError = "Не удалось прочитать активный профиль"
                    updateState { mapState() }
                }
        }
        blockScope?.launch {
            combine(
                getVpnStatusFlowUseCase(),
                getActiveProfileFlowUseCase(),
            ) { vpnStatus, profile -> vpnStatus to profile }
                .collect { (vpnStatus, profile) ->
                    status = vpnStatus
                    activeProfile = profile
                    updateState { mapState() }
                }
        }
    }

    fun start() {
        if (activeProfile != null) startVpnUseCase()
    }

    private fun requestStart() {
        if (activeProfile != null && status == VpnStatus.Disconnected) {
            onEvent(RequestVpnPermissionUiEvent)
        }
    }

    private fun mapState() = mapper.map(
        status = status,
        profile = activeProfile,
        error = profileError,
        onStart = ::requestStart,
        onStop = stopVpnUseCase::invoke,
    )
}
