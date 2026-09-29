package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.DeleteProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DeleteProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : DeleteProfileUseCase {
    override suspend fun invoke(id: String) {
        return withContext(Dispatchers.IO) {
            profileRepository.delete(id)
        }
    }
}