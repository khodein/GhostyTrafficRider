package com.ghosty.traffic.rider.main.presentation

import androidx.lifecycle.ViewModel
import com.ghosty.traffic.rider.framework.router.Router

internal class MainViewModel(
    private val router: Router,
) : ViewModel() {

    fun getBackStack() = router.getBackStack()
    fun goBack() = router.goBack()
}
