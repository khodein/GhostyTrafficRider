package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetAllProfileFlowUseCase
import kotlinx.coroutines.flow.Flow

internal class GetAllProfileFlowUseCaseImpl(
    private val repository: ProfileRepository
) : GetAllProfileFlowUseCase {

    override fun invoke(): Flow<List<ProfileModel>> {
        return repository.getAllFlow()
    }
}