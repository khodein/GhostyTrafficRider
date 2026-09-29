package com.ghosty.traffic.rider.feature.selfprofile.domain

import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.DeleteProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.DeleteProfileUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetActiveProfileFlowUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetActiveProfileFlowUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetAllProfileFlowUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetAllProfileFlowUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetProfileByIdUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetProfileByIdUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.LoadProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.LoadProfileUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.ParseProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.ParseProfileUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.SelectProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.SelectProfileUseCaseImpl
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.SetProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.SetProfileUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

internal object ProfileDomainModule {

    fun get(module: Module) = with(module) {
        addUseCase()
    }

    private fun Module.addUseCase() {
        factoryOf(::GetAllProfileFlowUseCaseImpl) bind GetAllProfileFlowUseCase::class
        factoryOf(::GetActiveProfileFlowUseCaseImpl) bind GetActiveProfileFlowUseCase::class
        factoryOf(::LoadProfileUseCaseImpl) bind LoadProfileUseCase::class
        factoryOf(::GetProfileByIdUseCaseImpl) bind GetProfileByIdUseCase::class
        factoryOf(::SetProfileUseCaseImpl) bind SetProfileUseCase::class
        factoryOf(::SelectProfileUseCaseImpl) bind SelectProfileUseCase::class
        factoryOf(::DeleteProfileUseCaseImpl) bind DeleteProfileUseCase::class
        factoryOf(::ParseProfileUseCaseImpl) bind ParseProfileUseCase::class
    }
}
