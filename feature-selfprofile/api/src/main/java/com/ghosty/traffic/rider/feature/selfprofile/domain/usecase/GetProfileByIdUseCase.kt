package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel

interface GetProfileByIdUseCase {
    suspend operator fun invoke(id: String): ProfileModel?
}
