package com.shreeganesh.ganapatimanagement.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shreeganesh.ganapatimanagement.ui.screens.aarti.AartiScreen
import com.shreeganesh.ganapatimanagement.ui.screens.donation.DonationScreen
import com.shreeganesh.ganapatimanagement.ui.screens.expense.ExpenseScreen
import com.shreeganesh.ganapatimanagement.ui.screens.home.HomeScreen
import com.shreeganesh.ganapatimanagement.ui.screens.karyakram.KaryakramScreen
import com.shreeganesh.ganapatimanagement.ui.screens.member.MemberScreen
import com.shreeganesh.ganapatimanagement.ui.viewmodel.GanapatiViewModel

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

    val context = LocalContext.current.applicationContext as Application
    val viewModel: GanapatiViewModel = viewModel(factory = GanapatiViewModel.Factory(context))

    // Real-time Database state observed via StateFlow
    val donationsList by viewModel.donations.collectAsState()
    val expensesList by viewModel.expenses.collectAsState()
    val membersList by viewModel.members.collectAsState()
    val karyakramList by viewModel.karyakramList.collectAsState()
    val aartiList by viewModel.aartis.collectAsState()
    val announcement by viewModel.announcement.collectAsState()
    val festivalSummary by viewModel.festivalSummary.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()
    val todaySchedule by viewModel.todaySchedule.collectAsState()

    Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
        when (screen) {
            Screen.Home -> {
                HomeScreen(
                    modifier = modifier,
                    summary = festivalSummary,
                    announcement = announcement,
                    recentActivities = recentActivities,
                    todaySchedule = todaySchedule,
                    onNavigateToDonations = {
                        currentScreen = Screen.Donations
                    },
                    onNavigateToExpenses = {
                        currentScreen = Screen.Expenses
                    },
                    onNavigateToMembers = {
                        currentScreen = Screen.Members
                    },
                    onNavigateToTasks = {
                        currentScreen = Screen.Karyakram
                    },
                    onNavigateToEvents = {
                        currentScreen = Screen.Karyakram
                    },
                    onNavigateToAarti = { aartiId ->
                        currentScreen = Screen.Aarti(aartiId)
                    }
                )
            }

            Screen.Donations -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                DonationScreen(
                    modifier = modifier,
                    donations = donationsList,
                    onAddDonation = { newDonation ->
                        viewModel.addDonation(newDonation)
                    },
                    onDeleteDonation = { donationId ->
                        viewModel.deleteDonation(donationId)
                    },
                    onBackClick = {
                        currentScreen = Screen.Home
                    }
                )
            }

            Screen.Expenses -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                ExpenseScreen(
                    modifier = modifier,
                    expenses = expensesList,
                    onAddExpense = { newExpense ->
                        viewModel.addExpense(newExpense)
                    },
                    onDeleteExpense = { expenseId ->
                        viewModel.deleteExpense(expenseId)
                    },
                    onBackClick = {
                        currentScreen = Screen.Home
                    }
                )
            }

            Screen.Members -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                MemberScreen(
                    modifier = modifier,
                    members = membersList,
                    onAddMember = { newMember ->
                        viewModel.addMember(newMember)
                    },
                    onDeleteMember = { memberId ->
                        viewModel.deleteMember(memberId)
                    },
                    onBackClick = {
                        currentScreen = Screen.Home
                    }
                )
            }

            Screen.Karyakram -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                KaryakramScreen(
                    modifier = modifier,
                    karyakramList = karyakramList,
                    onAddKaryakram = { newItem ->
                        viewModel.addKaryakram(newItem)
                    },
                    onUpdateKaryakramStatus = { id, status ->
                        viewModel.updateKaryakramStatus(id, status)
                    },
                    onDeleteKaryakram = { itemId ->
                        viewModel.deleteKaryakram(itemId)
                    },
                    onBackClick = {
                        currentScreen = Screen.Home
                    },
                    onPlayAarti = { aartiId ->
                        currentScreen = Screen.Aarti(aartiId)
                    }
                )
            }

            is Screen.Aarti -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                AartiScreen(
                    modifier = modifier,
                    aartiList = aartiList,
                    initialAartiId = screen.aartiId,
                    onBackClick = {
                        currentScreen = Screen.Home
                    }
                )
            }
        }
    }
}
