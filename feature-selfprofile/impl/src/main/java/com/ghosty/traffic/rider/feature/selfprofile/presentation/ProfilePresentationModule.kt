package com.ghosty.traffic.rider.feature.selfprofile.presentation

import com.ghosty.traffic.rider.feature.selfprofile.presentation.mapper.ProfileErrorMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.mapper.ProfileParseErrorMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.ProfileListViewModel
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.ProfileListBottomBarBlock
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.mapper.ProfileListBottomBarBlockMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.ProfileListBlock
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.mapper.ProfileListBlockMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.ProfileListTopBarBlock
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.mapper.ProfileListTopBarBlockMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf

internal object ProfilePresentationModule {

    fun get(module: Module) = with(module) {
        addViewModels()
        addBlocks()
        addMappers()
    }

    private fun Module.addMappers() {
        factoryOf(::ProfileParseErrorMapper)
        factoryOf(::ProfileErrorMapper)
        factoryOf(::ProfileListBlockMapper)
        factoryOf(::ProfileListBottomBarBlockMapper)
        factoryOf(::ProfileListTopBarBlockMapper)
    }


    private fun Module.addViewModels() {
        viewModelOf(::ProfileListViewModel)
    }

    private fun Module.addBlocks() {
        factoryOf(::ProfileListBlock)
        factoryOf(::ProfileListTopBarBlock)
        factoryOf(::ProfileListBottomBarBlock)
    }
}