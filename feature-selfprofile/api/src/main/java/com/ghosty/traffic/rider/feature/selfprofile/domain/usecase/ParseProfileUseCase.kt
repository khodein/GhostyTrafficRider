package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel

interface ParseProfileUseCase {
    suspend operator fun invoke(text: String): ProfileModel
}