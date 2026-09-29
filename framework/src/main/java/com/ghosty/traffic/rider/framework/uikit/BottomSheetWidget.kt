package com.ghosty.traffic.rider.framework.uikit

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghosty.traffic.rider.framework.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWidget(
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = true,
    modalBottomSheetProperties: ModalBottomSheetProperties,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        shape = AppTheme.corner.top(AppTheme.corner.large()),
        containerColor = AppTheme.color.surface,
        contentColor = AppTheme.color.onSurface,
        tonalElevation = 0.dp,
        properties = modalBottomSheetProperties,
        dragHandle = null,
        content = content,
    )
}
