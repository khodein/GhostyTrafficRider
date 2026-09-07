package com.ghosty.traffic.rider.main

import com.ghosty.traffic.rider.main.presentation.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object MainModule {
    fun get() = module {
        viewModelOf(::MainViewModel)
    }
}
