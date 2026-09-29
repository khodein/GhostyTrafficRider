package com.ghosty.traffic.rider.framework.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghosty.traffic.rider.framework.theme.AppTheme

@Composable
fun AppButtonWidget(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier.height(48.dp),
        enabled = enabled,
        onClick = onClick,
        shape = AppTheme.corner.all(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AppTheme.color.primary,
            disabledContentColor = AppTheme.color.onSurfaceVariant,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) AppTheme.color.primary else AppTheme.color.outlineVariant,
        ),
        contentPadding = PaddingValues(
            horizontal = AppTheme.padding.medium(),
            vertical = AppTheme.padding.small(),
        ),
    ) {
        Text(
            text = text,
            style = AppTheme.typography.titleMedium,
        )
    }
}
