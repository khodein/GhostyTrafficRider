package com.ghosty.traffic.rider.feature.profile.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghosty.traffic.rider.framework.UiStatus
import com.ghosty.traffic.rider.framework.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileListViewModel>()
    val state by viewModel.viewState.collectAsState()
    LaunchedEffect(viewModel) { viewModel.attach() }

    if (state.editing) {
        ProfileEditor(state, viewModel)
        return
    }

    Scaffold { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets).padding(AppTheme.padding.medium()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.padding.medium()),
        ) {
            Text("Профили", style = AppTheme.typography.headlineLarge)
            Text("Добавьте конфигурацию сервера и выберите профиль", style = AppTheme.typography.bodyMedium)
            Button(
                onClick = viewModel::openEditor,
                enabled = !state.busy && state.status == UiStatus.Success,
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(AppTheme.padding.small()))
                Text("Вставить YAML")
            }
            if (state.status == UiStatus.Loading || state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
            if (!state.editing) state.error?.let { Text(it, color = AppTheme.color.error) }
            if (state.status == UiStatus.Error) {
                TextButton(onClick = viewModel::attach) { Text("Повторить") }
            }
            if (state.status == UiStatus.Success && state.profiles.isEmpty()) {
                Text("Профилей пока нет. Нажмите «Вставить YAML», чтобы добавить первый.")
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.padding.small()),
            ) {
                items(state.profiles, key = { it.id }) { profile ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(AppTheme.padding.small()),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = profile.id == state.activeId,
                                onClick = { viewModel.select(profile.id) },
                                enabled = !state.busy,
                            )
                            Column(Modifier.weight(1f)) {
                                Text(profile.name, style = AppTheme.typography.titleMedium)
                                Text("${profile.type} · ${profile.server}:${profile.port}", style = AppTheme.typography.bodySmall)
                                if (profile.id == state.activeId) Text("Выбран", color = AppTheme.color.primary)
                            }
                            IconButton(onClick = { viewModel.edit(profile.id) }, enabled = !state.busy) {
                                Icon(Icons.Default.Edit, contentDescription = "Редактировать профиль")
                            }
                            IconButton(onClick = { viewModel.delete(profile.id) }, enabled = !state.busy) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить профиль")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileEditor(state: ProfileListState, viewModel: ProfileListViewModel) {
    BackHandler { viewModel.closeEditor() }
    Scaffold { insets ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(insets).imePadding().padding(AppTheme.padding.medium()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.padding.small()),
        ) {
            item {
                Text(
                    if (state.editingId == null) "Новый профиль" else "Редактирование профиля",
                    style = AppTheme.typography.headlineMedium,
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.padding.small())) {
                    TextButton(onClick = viewModel::closeEditor, enabled = !state.busy) { Text("Назад") }
                    TextButton(onClick = viewModel::format, enabled = !state.busy && state.draft.isNotBlank()) {
                        Text("Форматировать")
                    }
                    Button(onClick = viewModel::save, enabled = !state.busy && state.draft.isNotBlank()) {
                        Text("Сохранить")
                    }
                }
            }
            state.error?.let { error -> item { Text(error, color = AppTheme.color.error) } }
            if (state.busy) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            item {
                OutlinedTextField(
                    value = state.draft,
                    onValueChange = viewModel::changeDraft,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 320.dp),
                    label = { Text("YAML-конфигурация") },
                    textStyle = AppTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                    enabled = !state.busy,
                    isError = state.error != null,
                )
            }
        }
    }
}
