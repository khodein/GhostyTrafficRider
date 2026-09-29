package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetActiveProfileFlowUseCase
import kotlinx.coroutines.flow.StateFlow

internal class GetActiveProfileFlowUseCaseImpl(
    private val repository: ProfileRepository
) : GetActiveProfileFlowUseCase {
    override fun invoke(): StateFlow<ProfileModel?> {
        return repository.getActiveFlow()
    }
}
