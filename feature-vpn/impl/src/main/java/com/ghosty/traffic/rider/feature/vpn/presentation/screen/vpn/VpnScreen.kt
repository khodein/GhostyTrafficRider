package com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn

import android.app.Activity
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.ghosty.traffic.rider.feature.vpn.presentation.screen.vpn.blocks.main.widget.VpnMainWidget
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun VpnScreen() {
    val viewModel = koinViewModel<VpnViewModel>()
    val state by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) viewModel.start()
    }

    LaunchedEffect(viewModel, context) {
        viewModel.uiEvent.collect { event ->
            if (event == RequestVpnPermissionUiEvent) {
                val permissionIntent = VpnService.prepare(context)
                if (permissionIntent == null) {
                    viewModel.start()
                } else {
                    permissionLauncher.launch(permissionIntent)
                }
            }
        }
    }

    VpnMainWidget(uiState = state.main)
}
