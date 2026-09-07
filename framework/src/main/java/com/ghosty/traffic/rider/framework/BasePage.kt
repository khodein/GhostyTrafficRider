package com.ghosty.traffic.rider.framework

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun <T> PageList(
    modifier: Modifier = Modifier,
    state: PageState<T>,
    loadThreshold: Int = 5,
    onNextAction: (page: Int) -> Unit,
    contentKey: (T) -> Any = { it.hashCode() },
    contentType: ((T) -> Any?) = { "DEFAULT_ITEM" },
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
    headerContent: LazyListScope.() -> Unit = { },
    loadContent: LazyListScope.() -> Unit = { },
    errorContent: LazyListScope.(page: Int) -> Unit = { page -> },
) {
    val sourceState = rememberLazyListState()
    val pageState by rememberUpdatedState(state)
    val onNextState by rememberUpdatedState(onNextAction)

    LaunchedEffect(sourceState) {
        snapshotFlow {
            val visibleInfo = sourceState.layoutInfo.visibleItemsInfo
            val lastIndex = visibleInfo.lastOrNull()?.index ?: 0
            val totalCount = sourceState.layoutInfo.totalItemsCount

            val isUploadMore = lastIndex >= (totalCount - loadThreshold)
            val isNeededMore = visibleInfo.isNotEmpty() &&
                    pageState.status == UiStatus.Success &&
                    !pageState.isEndReached

            isUploadMore && isNeededMore
        }
            .distinctUntilChanged()
            .collect { isLoadMore ->
                if (isLoadMore) onNextState.invoke(pageState.page + 1)
            }
    }

    LazyColumn(
        state = sourceState,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        headerContent()

        items(
            items = pageState.items,
            key = contentKey,
            contentType = contentType,
            itemContent = itemContent,
        )

        when (pageState.status) {
            UiStatus.Loading -> loadContent()
            UiStatus.Error -> errorContent(pageState.page)
            else -> Unit
        }
    }
}
