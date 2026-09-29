package com.ghosty.traffic.rider.feature.vpn.domain.usecase

import com.ghosty.traffic.rider.feature.vpn.data.repository.vpn.VpnRepository

internal class StartVpnUseCaseImpl(
    private val repository: VpnRepository,
) : StartVpnUseCase {
    override fun invoke() {
        repository.start()
    }
}
