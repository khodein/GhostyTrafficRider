package com.ghosty.traffic.rider.main.presentation

import android.app.Activity
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ghosty.traffic.rider.feature.profile.model.ProxyProfile
import com.ghosty.traffic.rider.feature.profile.storage.ProfileStorage
import com.ghosty.traffic.rider.framework.theme.AppTheme
import com.ghosty.traffic.rider.vpn.VpnConnection
import com.ghosty.traffic.rider.vpn.VpnStatus
import kotlinx.coroutines.flow.catch
import org.koin.compose.koinInject

@Composable
internal fun VpnPanel() {
    val context = LocalContext.current
    val storage = koinInject<ProfileStorage>()
    var active by remember { mutableStateOf<ProxyProfile?>(null) }
    var pending by remember { mutableStateOf<ProxyProfile?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val state by VpnConnection.state.collectAsState()
    LaunchedEffect(storage) {
        storage.getActive().catch { error = "Не удалось прочитать активный профиль" }.collect { active = it }
    }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val profile = pending
        pending = null
        if (result.resultCode == Activity.RESULT_OK && profile != null) {
            VpnConnection.start(context, profile)
        } else if (result.resultCode != Activity.RESULT_OK) {
            error = "Разрешение VPN не предоставлено"
        }
    }
    Surface(color = AppTheme.color.surfaceVariant) {
        Column(Modifier.fillMaxWidth().statusBarsPadding().padding(AppTheme.padding.medium())) {
            Text("VPN: ${state.status}", style = AppTheme.typography.titleMedium)
            Text(state.profileName ?: active?.name ?: "Выберите профиль")
            (error ?: state.error)?.let { Text(it, color = AppTheme.color.error) }
            if (state.status == VpnStatus.DISCONNECTED) {
                Button(enabled = active != null && pending == null, onClick = {
                    error = null
                    val profile = active ?: return@Button
                    val request = VpnService.prepare(context)
                    if (request == null) VpnConnection.start(context, profile) else {
                        pending = profile
                        permission.launch(request)
                    }
                }) { Text("START") }
            } else {
                Button(enabled = state.status != VpnStatus.STOPPING, onClick = { VpnConnection.stop(context) }) {
                    Text("STOP")
                }
                Text("Изменения профиля применятся после STOP → START", style = AppTheme.typography.bodySmall)
            }
        }
    }
}
