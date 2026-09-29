package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.widget.ProfileListBottomBarWidget
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.widget.ProfileListWidget
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.widget.ProfileListTopBarWidget
import com.ghosty.traffic.rider.framework.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProfileListScreen() {
    val viewModel = koinViewModel<ProfileListViewModel>()
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetch()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.color.surface,
        topBar = {
            ProfileListTopBarWidget(
                uiState = state.profileListTopBarState
            )
        },
        bottomBar = {
            ProfileListBottomBarWidget(
                uiState = state.profileListBottomBarState
            )
        }
    ) { paddingValues ->
        ProfileListWidget(
            modifier = Modifier.fillMaxSize(),
            paddingValues = paddingValues,
            uiState = state.profileListState
        )
    }
}
