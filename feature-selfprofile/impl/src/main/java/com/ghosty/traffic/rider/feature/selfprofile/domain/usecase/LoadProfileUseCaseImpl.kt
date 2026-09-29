package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LoadProfileUseCaseImpl(
    private val repository: ProfileRepository
) : LoadProfileUseCase {
    override suspend fun invoke(): List<ProfileModel> {
        return withContext(Dispatchers.IO)  {
            repository.load()
        }
    }
}
