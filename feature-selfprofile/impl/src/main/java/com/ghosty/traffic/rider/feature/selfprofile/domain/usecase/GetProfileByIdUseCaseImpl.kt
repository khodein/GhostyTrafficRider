package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetProfileByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GetProfileByIdUseCaseImpl(
    private val repository: ProfileRepository
) : GetProfileByIdUseCase {
    override suspend fun invoke(id: String): ProfileModel? {
        return withContext(Dispatchers.IO) {
            repository.getById(id)
        }
    }
}
