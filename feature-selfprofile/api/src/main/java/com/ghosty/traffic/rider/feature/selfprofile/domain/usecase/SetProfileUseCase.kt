package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel

interface SetProfileUseCase {
    suspend operator fun invoke(model: ProfileModel)
}