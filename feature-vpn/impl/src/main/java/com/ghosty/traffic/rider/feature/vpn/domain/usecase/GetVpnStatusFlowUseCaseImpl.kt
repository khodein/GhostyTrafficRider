package com.ghosty.traffic.rider.feature.vpn.domain.usecase

import com.ghosty.traffic.rider.feature.vpn.data.repository.vpn.VpnRepository
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnStatus
import kotlinx.coroutines.flow.Flow

internal class GetVpnStatusFlowUseCaseImpl(
    private val repository: VpnRepository,
) : GetVpnStatusFlowUseCase {
    override fun invoke(): Flow<VpnStatus> = repository.getStatusFlow()
}
