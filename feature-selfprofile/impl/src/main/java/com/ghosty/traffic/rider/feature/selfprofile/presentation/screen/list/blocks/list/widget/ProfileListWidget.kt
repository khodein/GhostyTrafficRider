package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState.Item
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileUiState

@Composable
internal fun ProfileListWidget(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues.Zero,
    uiState: ProfileListUiState,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = paddingValues,
    ) {
        items(
            items = uiState.items,
            key = { item -> item.key },
            contentType = { item -> item.contentType }
        ) { item ->
            when (item) {
                is ProfileUiState -> {
                    ProfileWidget(
                        modifier = Modifier,
                        uiState = item,
                    )
                }
            }
        }
    }
}

