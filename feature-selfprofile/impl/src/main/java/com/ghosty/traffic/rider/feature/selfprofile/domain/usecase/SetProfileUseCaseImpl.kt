package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class SetProfileUseCaseImpl(
    private val repository: ProfileRepository
) : SetProfileUseCase {
    override suspend fun invoke(model: ProfileModel) {
        return withContext(Dispatchers.IO) {
            repository.save(model)
        }
    }
}