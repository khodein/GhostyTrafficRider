package com.ghosty.traffic.rider.framework.tools

import com.ghosty.traffic.rider.framework.tools.res.ResProvider
import com.ghosty.traffic.rider.framework.tools.res.ResProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ResModule {
    fun get() = module {
        single<ResProvider> { ResProviderImpl(androidContext()) }
    }
}
