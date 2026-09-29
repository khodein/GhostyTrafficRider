package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.coroutines.flow.StateFlow

interface GetActiveProfileFlowUseCase {
    operator fun invoke(): StateFlow<ProfileModel?>
}
