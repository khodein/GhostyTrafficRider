package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.state.VpnMainUiState
import com.ghosty.traffic.rider.framework.theme.AppTheme

@Composable
internal fun VpnMainWidget(
    uiState: VpnMainUiState,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier, color = AppTheme.color.surfaceVariant) {
        Column(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(AppTheme.padding.medium())
        ) {
            Text("VPN: ${uiState.status}", style = AppTheme.typography.titleMedium)
            Text(uiState.profileName)
            uiState.error?.let { error ->
                Text(error, color = AppTheme.color.error)
            }
            Button(
                enabled = uiState.isActionEnabled,
                onClick = uiState.actionClickEvent,
            ) {
                Text(if (uiState.isStartVisible) "START" else "STOP")
            }
        }
    }
}
