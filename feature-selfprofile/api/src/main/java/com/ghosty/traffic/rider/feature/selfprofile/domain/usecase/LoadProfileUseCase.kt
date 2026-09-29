package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel

interface LoadProfileUseCase {
    suspend operator fun invoke(): List<ProfileModel>
}
