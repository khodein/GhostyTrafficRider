package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class SelectProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : SelectProfileUseCase {
    override suspend fun invoke(id: String?) {
        return withContext(Dispatchers.IO) {
            repository.select(id)
        }
    }
}