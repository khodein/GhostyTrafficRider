package com.ghosty.traffic.rider.framework.uikit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghosty.traffic.rider.framework.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarWidget(
    modifier: Modifier = Modifier,
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = AppTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = AppTheme.color.onSurface,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.color.surface,
            titleContentColor = AppTheme.color.onSurface,
            navigationIconContentColor = AppTheme.color.onSurface,
        ),
    )
}
