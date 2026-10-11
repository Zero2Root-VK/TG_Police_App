package com.tgp.mockexam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.tgp.mockexam.bootstrap.BootstrapState
import com.tgp.mockexam.bootstrap.BootstrapViewModel
import com.tgp.mockexam.core.designsystem.theme.TgpTheme
import com.tgp.mockexam.core.domain.repository.AuthRepository
import com.tgp.mockexam.core.network.AppwriteConfig as NetworkConfig
import com.tgp.mockexam.feature.auth.AuthScreen
import com.tgp.mockexam.feature.auth.OnboardingScreen
import com.tgp.mockexam.feature.auth.ProfileScreen
import com.tgp.mockexam.feature.catalog.CatalogScreen
import com.tgp.mockexam.feature.exam.ExamRoute
import com.tgp.mockexam.feature.home.HomeScreen
import com.tgp.mockexam.feature.currentaffairs.CurrentAffairsScreen
import com.tgp.mockexam.feature.admin.AdminTreeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
private data object HomeRoute : NavKey

@Serializable
private data object TestsRoute : NavKey

@Serializable
private data object CurrentAffairsRoute : NavKey

@Serializable
private data object AdminRoute : NavKey

private data object ProfileRoute : NavKey

@Serializable
private data object OnboardingRoute : NavKey

@Serializable
private data object AuthRoute : NavKey

@Serializable
private data class ExamKey(val paperId: String) : NavKey

private data class BottomTab(
    val route: NavKey,
    val label: String,
    val icon: ImageVector,
)

private val topLevelRoutes: List<NavKey> = listOf(
    HomeRoute,
    TestsRoute,
    CurrentAffairsRoute,
    ProfileRoute,
)

private val bottomTabs: List<BottomTab> = listOf(
    BottomTab(HomeRoute, "Home", Icons.Filled.Home),
    BottomTab(TestsRoute, "Tests", Icons.AutoMirrored.Filled.List),
    BottomTab(CurrentAffairsRoute, "Current Affairs", Icons.Filled.Notifications),
    BottomTab(ProfileRoute, "Profile", Icons.Filled.Person),
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleAuthCallback(intent)

        val bootstrapped = mutableStateOf(false)
        splash.setKeepOnScreenCondition { !bootstrapped.value }

        setContent {
            TgpTheme {
                val viewModel: BootstrapViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(state) {
                    if (state != BootstrapState.Loading) bootstrapped.value = true
                }

                when (state) {
                    BootstrapState.Loading -> Unit
                    BootstrapState.Onboarding -> TgpApp(initialRoute = OnboardingRoute)
                    BootstrapState.Main -> TgpApp(initialRoute = HomeRoute)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthCallback(intent)
    }

    /** Completes a social / magic-link sign-in when the browser redirects back to the app. */
    private fun handleAuthCallback(intent: Intent?) {
        val data = intent?.data ?: return
        if (data.scheme != NetworkConfig.CALLBACK_SCHEME) return
        lifecycleScope.launch {
            authRepository.completeOAuthCallback(data.toString())
        }
    }
}

@Composable
private fun TgpApp(initialRoute: NavKey) {
    val backStack = rememberNavBackStack(initialRoute)
    val current = backStack.lastOrNull()

    // Swap the top entry without ever leaving the stack empty (NavDisplay forbids emptiness).
    fun replaceTop(route: NavKey) {
        if (backStack.isEmpty()) backStack.add(route) else backStack[backStack.lastIndex] = route
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (current in topLevelRoutes) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = current == tab.route,
                            onClick = {
                                if (current != tab.route) {
                                    backStack.clear()
                                    backStack.add(tab.route)
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<OnboardingRoute> {
                    OnboardingScreen(onFinish = { replaceTop(AuthRoute) })
                }
                entry<AuthRoute> {
                    AuthScreen(
                        onAuthenticated = { replaceTop(HomeRoute) },
                        onSkip = { replaceTop(HomeRoute) },
                    )
                }
                entry<HomeRoute> {
                    HomeScreen(
                        onOpenTrack = { backStack.add(TestsRoute) },
                        onBrowsePapers = { backStack.add(TestsRoute) },
                        onOpenCurrentAffairs = {
                            backStack.clear()
                            backStack.add(CurrentAffairsRoute)
                        },
                        onOpenProfile = {
                            backStack.clear()
                            backStack.add(ProfileRoute)
                        },
                    )
                }
                entry<TestsRoute> {
                    CatalogScreen(
                        onPaperClick = { paper -> backStack.add(ExamKey(paper.id)) },
                    )
                }
                entry<CurrentAffairsRoute> { CurrentAffairsScreen(onBack = { backStack.removeLastOrNull() }) }
                entry<AdminRoute> {
                    AdminTreeScreen(onBack = { if (backStack.size > 1) backStack.removeLastOrNull() })
                }
                entry<ProfileRoute> {
                    ProfileScreen(onSignIn = { backStack.add(AuthRoute) }, onOpenAdmin = { backStack.add(AdminRoute) })
                }
                entry<ExamKey> { key -> ExamRoute(paperId = key.paperId) }
            },
        )
    }
}
