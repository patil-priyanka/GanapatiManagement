package com.shreeganesh.ganapatimanagement.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shreeganesh.ganapatimanagement.core.connectivity.NetworkConnectivityObserver
import com.shreeganesh.ganapatimanagement.ui.components.LoadingOverlay
import com.shreeganesh.ganapatimanagement.ui.components.OfflineBanner
import com.shreeganesh.ganapatimanagement.ui.screens.aarti.AartiScreen
import com.shreeganesh.ganapatimanagement.ui.screens.aarti.AartiViewModel
import com.shreeganesh.ganapatimanagement.ui.screens.donation.DonationScreen
import com.shreeganesh.ganapatimanagement.ui.screens.donation.DonationViewModel
import com.shreeganesh.ganapatimanagement.ui.screens.expense.ExpenseScreen
import com.shreeganesh.ganapatimanagement.ui.screens.expense.ExpenseViewModel
import com.shreeganesh.ganapatimanagement.ui.screens.home.HomeScreen
import com.shreeganesh.ganapatimanagement.ui.screens.home.HomeViewModel
import com.shreeganesh.ganapatimanagement.ui.screens.karyakram.KaryakramScreen
import com.shreeganesh.ganapatimanagement.ui.screens.karyakram.KaryakramViewModel
import com.shreeganesh.ganapatimanagement.ui.screens.member.MemberScreen
import com.shreeganesh.ganapatimanagement.ui.screens.member.MemberViewModel

sealed interface Screen {
    object Home : Screen
    object Donations : Screen
    object Expenses : Screen
    object Members : Screen
    object Karyakram : Screen
    data class Aarti(val aartiId: String? = null) : Screen
}

@Composable
fun GanapatiApp(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current

    // ── Connectivity ─────────────────────────────────────────────────────────
    val connectivityObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by connectivityObserver.isConnected.collectAsState(initial = true)
    val isOffline = !isConnected

    // ── App-level Snackbar (errors from any screen) ───────────────────────────
    val snackbarHostState = remember { SnackbarHostState() }

    Column(modifier = modifier.fillMaxSize()) {

        // Offline banner — always visible at the very top when offline
        OfflineBanner(isOffline = isOffline)

        // Screen content + loading overlay
        Box(modifier = Modifier.weight(1f)) {

            Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
                when (screen) {

                    // ── Home ─────────────────────────────────────────────────
                    Screen.Home -> {
                        val viewModel: HomeViewModel = viewModel(
                            factory = HomeViewModel.Factory(application)
                        )
                        val uiState by viewModel.uiState.collectAsState()

                        // Surface error message via Snackbar
                        LaunchedEffect(uiState.errorMessage) {
                            uiState.errorMessage?.let { msg ->
                                snackbarHostState.showSnackbar(msg)
                                viewModel.clearError()
                            }
                        }

                        HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            summary = uiState.festivalSummary,
                            announcement = uiState.announcement,
                            recentActivities = uiState.recentActivities,
                            todaySchedule = uiState.todaySchedule,
                            onNavigateToDonations = { currentScreen = Screen.Donations },
                            onNavigateToExpenses = { currentScreen = Screen.Expenses },
                            onNavigateToMembers = { currentScreen = Screen.Members },
                            onNavigateToTasks = { currentScreen = Screen.Karyakram },
                            onNavigateToEvents = { currentScreen = Screen.Karyakram },
                            onNavigateToAarti = { aartiId -> currentScreen = Screen.Aarti(aartiId) }
                        )

                        if (uiState.isLoading) LoadingOverlay()
                    }

                    // ── Donations ─────────────────────────────────────────────
                    Screen.Donations -> {
                        BackHandler { currentScreen = Screen.Home }
                        val viewModel: DonationViewModel = viewModel(
                            factory = DonationViewModel.Factory(application)
                        )
                        val uiState by viewModel.uiState.collectAsState()

                        LaunchedEffect(uiState.errorMessage) {
                            uiState.errorMessage?.let { msg ->
                                snackbarHostState.showSnackbar(msg)
                                viewModel.clearError()
                            }
                        }

                        DonationScreen(
                            modifier = Modifier.fillMaxSize(),
                            donations = uiState.donations,
                            onAddDonation = { viewModel.addDonation(it) },
                            onDeleteDonation = { viewModel.deleteDonation(it) },
                            onBackClick = { currentScreen = Screen.Home }
                        )

                        if (uiState.isLoading) LoadingOverlay()
                    }

                    // ── Expenses ──────────────────────────────────────────────
                    Screen.Expenses -> {
                        BackHandler { currentScreen = Screen.Home }
                        val viewModel: ExpenseViewModel = viewModel(
                            factory = ExpenseViewModel.Factory(application)
                        )
                        val uiState by viewModel.uiState.collectAsState()

                        LaunchedEffect(uiState.errorMessage) {
                            uiState.errorMessage?.let { msg ->
                                snackbarHostState.showSnackbar(msg)
                                viewModel.clearError()
                            }
                        }

                        ExpenseScreen(
                            modifier = Modifier.fillMaxSize(),
                            expenses = uiState.expenses,
                            onAddExpense = { viewModel.addExpense(it) },
                            onDeleteExpense = { viewModel.deleteExpense(it) },
                            onBackClick = { currentScreen = Screen.Home }
                        )

                        if (uiState.isLoading) LoadingOverlay()
                    }

                    // ── Members ───────────────────────────────────────────────
                    Screen.Members -> {
                        BackHandler { currentScreen = Screen.Home }
                        val viewModel: MemberViewModel = viewModel(
                            factory = MemberViewModel.Factory(application)
                        )
                        val uiState by viewModel.uiState.collectAsState()

                        LaunchedEffect(uiState.errorMessage) {
                            uiState.errorMessage?.let { msg ->
                                snackbarHostState.showSnackbar(msg)
                                viewModel.clearError()
                            }
                        }

                        MemberScreen(
                            modifier = Modifier.fillMaxSize(),
                            members = uiState.members,
                            onAddMember = { viewModel.addMember(it) },
                            onDeleteMember = { viewModel.deleteMember(it) },
                            onBackClick = { currentScreen = Screen.Home }
                        )

                        if (uiState.isLoading) LoadingOverlay()
                    }

                    // ── Karyakram ─────────────────────────────────────────────
                    Screen.Karyakram -> {
                        BackHandler { currentScreen = Screen.Home }
                        val viewModel: KaryakramViewModel = viewModel(
                            factory = KaryakramViewModel.Factory(application)
                        )
                        val uiState by viewModel.uiState.collectAsState()

                        LaunchedEffect(uiState.errorMessage) {
                            uiState.errorMessage?.let { msg ->
                                snackbarHostState.showSnackbar(msg)
                                viewModel.clearError()
                            }
                        }

                        KaryakramScreen(
                            modifier = Modifier.fillMaxSize(),
                            karyakramList = uiState.karyakramList,
                            onAddKaryakram = { viewModel.addKaryakram(it) },
                            onUpdateKaryakramStatus = { id, status ->
                                viewModel.updateKaryakramStatus(id, status)
                            },
                            onDeleteKaryakram = { viewModel.deleteKaryakram(it) },
                            onBackClick = { currentScreen = Screen.Home },
                            onPlayAarti = { aartiId -> currentScreen = Screen.Aarti(aartiId) }
                        )

                        if (uiState.isLoading) LoadingOverlay()
                    }

                    // ── Aarti ─────────────────────────────────────────────────
                    is Screen.Aarti -> {
                        BackHandler { currentScreen = Screen.Home }
                        val viewModel: AartiViewModel = viewModel(
                            factory = AartiViewModel.Factory(application)
                        )
                        val uiState by viewModel.uiState.collectAsState()

                        LaunchedEffect(uiState.errorMessage) {
                            uiState.errorMessage?.let { msg ->
                                snackbarHostState.showSnackbar(msg)
                                viewModel.clearError()
                            }
                        }

                        AartiScreen(
                            modifier = Modifier.fillMaxSize(),
                            aartiList = uiState.aartis,
                            initialAartiId = screen.aartiId,
                            onBackClick = { currentScreen = Screen.Home }
                        )

                        if (uiState.isLoading) LoadingOverlay()
                    }
                }
            }

            // App-level Snackbar anchored at bottom of content area
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
