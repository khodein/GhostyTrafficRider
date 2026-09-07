package com.ghosty.traffic.rider.framework.router

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

interface Router {
    fun getBackStack(): List<NavKey>
    fun goTo(key: NavKey)
    fun goBack()

    interface Provider {
        operator fun invoke(): EntryProviderInstaller
    }
}
