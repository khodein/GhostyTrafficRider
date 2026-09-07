package com.ghosty.traffic.rider.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ghosty.traffic.rider.framework.router.NAV_TRANSITION_KEY
import com.ghosty.traffic.rider.framework.router.Router
import com.ghosty.traffic.rider.framework.theme.AppTheme
import com.ghosty.traffic.rider.router.RouterModule
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                GhostyTrafficRiderApp()
            }
        }
    }
}

@Composable
private fun GhostyTrafficRiderApp() {
    val viewModel = koinViewModel<MainViewModel>()

    NavDisplay(
        backStack = viewModel.getBackStack(),
        onBack = viewModel::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            RouterModule.enterContentTransform(
                targetState.entries.lastOrNull()?.metadata?.get(
                    NAV_TRANSITION_KEY
                )
            )
        },
        popTransitionSpec = {
            RouterModule.popContentTransform(
                initialState.entries.lastOrNull()?.metadata?.get(
                    NAV_TRANSITION_KEY
                )
            )
        },
        predictivePopTransitionSpec = {
            RouterModule.popContentTransform(
                initialState.entries.lastOrNull()?.metadata?.get(
                    NAV_TRANSITION_KEY
                )
            )
        },
        entryProvider = entryProvider {
            getKoin()
                .getAll<Router.Provider>()
                .forEach { it.invoke().invoke(this) }
        }
    )
}
