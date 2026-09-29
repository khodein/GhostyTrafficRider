package com.ghosty.traffic.rider.feature.selfprofile.di

import com.ghosty.traffic.rider.feature.selfprofile.data.ProfileDataModule
import com.ghosty.traffic.rider.feature.selfprofile.domain.ProfileDomainModule
import com.ghosty.traffic.rider.feature.selfprofile.presentation.ProfilePresentationModule
import com.ghosty.traffic.rider.feature.selfprofile.router.ProfileRouterModule
import org.koin.dsl.module

object ProfileModule {

    fun get() = module {
        ProfileDataModule.get(this)
        ProfileDomainModule.get(this)
        ProfileRouterModule.get(this)
        ProfilePresentationModule.get(this)
    }
}