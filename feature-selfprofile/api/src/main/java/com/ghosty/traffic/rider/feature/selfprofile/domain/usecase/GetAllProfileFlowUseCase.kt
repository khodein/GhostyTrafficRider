package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface GetAllProfileFlowUseCase {
    operator fun invoke(): Flow<List<ProfileModel>>
}