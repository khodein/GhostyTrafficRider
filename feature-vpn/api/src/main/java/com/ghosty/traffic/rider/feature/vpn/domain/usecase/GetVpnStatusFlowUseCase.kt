package com.ghosty.traffic.rider.feature.vpn.domain.usecase

import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnStatus
import kotlinx.coroutines.flow.Flow

interface GetVpnStatusFlowUseCase {
    operator fun invoke(): Flow<VpnStatus>
}
