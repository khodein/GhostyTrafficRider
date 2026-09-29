package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

interface DeleteProfileUseCase {
    suspend operator fun invoke(id: String)
}