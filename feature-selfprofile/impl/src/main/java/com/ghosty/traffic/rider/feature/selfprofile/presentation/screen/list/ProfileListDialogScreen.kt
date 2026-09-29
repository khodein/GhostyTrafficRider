package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileListDialogScreen() {
    val viewModel = koinViewModel<ProfileListViewModel>()
    val state by viewModel.viewState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetch()
    }
}