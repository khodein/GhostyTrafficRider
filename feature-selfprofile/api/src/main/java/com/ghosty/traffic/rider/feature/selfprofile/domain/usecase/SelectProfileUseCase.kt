package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

interface SelectProfileUseCase {
    suspend operator fun invoke(id: String?)
}