package com.ghosty.traffic.rider.router

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import com.ghosty.traffic.rider.framework.router.Router
import com.ghosty.traffic.rider.framework.router.AppStartKey

internal class RouterImpl : Router {
    private val backStack: SnapshotStateList<NavKey> = mutableStateListOf(AppStartKey)

    override fun getBackStack(): List<NavKey> = backStack

    override fun goTo(key: NavKey) {
        backStack.add(key)
    }

    override fun goBack() {
        if (backStack.size > 1) backStack.removeLastOrNull()
    }
}
