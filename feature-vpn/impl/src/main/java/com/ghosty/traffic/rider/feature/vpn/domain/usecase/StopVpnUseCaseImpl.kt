package com.ghosty.traffic.rider.feature.vpn.domain.usecase

import com.ghosty.traffic.rider.feature.vpn.data.repository.vpn.VpnRepository

internal class StopVpnUseCaseImpl(
    private val repository: VpnRepository,
) : StopVpnUseCase {
    override fun invoke() {
        repository.stop()
    }
}
