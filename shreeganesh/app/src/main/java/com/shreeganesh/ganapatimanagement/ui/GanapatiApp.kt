package com.shreeganesh.ganapatimanagement.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shreeganesh.ganapatimanagement.model.Donation
import com.shreeganesh.ganapatimanagement.model.Expense
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import com.shreeganesh.ganapatimanagement.model.KaryakramItem
import com.shreeganesh.ganapatimanagement.model.Member
import com.shreeganesh.ganapatimanagement.model.getSampleDonations
import com.shreeganesh.ganapatimanagement.model.getSampleExpenses
import com.shreeganesh.ganapatimanagement.model.getSampleKaryakramList
import com.shreeganesh.ganapatimanagement.model.getSampleMembers
import com.shreeganesh.ganapatimanagement.ui.screens.aarti.AartiScreen
import com.shreeganesh.ganapatimanagement.ui.screens.donation.DonationScreen
import com.shreeganesh.ganapatimanagement.ui.screens.expense.ExpenseScreen
import com.shreeganesh.ganapatimanagement.ui.screens.home.HomeScreen
import com.shreeganesh.ganapatimanagement.ui.screens.karyakram.KaryakramScreen
import com.shreeganesh.ganapatimanagement.ui.screens.member.MemberScreen

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

    // Shared in-memory list of donations, expenses, members & festival schedule
    val donationsList = remember { mutableStateListOf<Donation>().apply { addAll(getSampleDonations()) } }
    val expensesList = remember { mutableStateListOf<Expense>().apply { addAll(getSampleExpenses()) } }
    val membersList = remember { mutableStateListOf<Member>().apply { addAll(getSampleMembers()) } }
    val karyakramList = remember { mutableStateListOf<KaryakramItem>().apply { addAll(getSampleKaryakramList()) } }

    // Dynamically calculate summary stats based on current donations, expenses and members
    val currentTotalDonation = donationsList.sumOf { it.amount }
    val currentDonorCount = donationsList.size
    val currentTotalExpense = expensesList.sumOf { it.amount }
    val currentVolunteerCount = membersList.size

    val festivalSummary = remember(currentTotalDonation, currentDonorCount, currentTotalExpense, currentVolunteerCount) {
        FestivalSummary(
            totalDonation = currentTotalDonation,
            donorCount = currentDonorCount,
            totalExpense = currentTotalExpense,
            volunteerCount = currentVolunteerCount
        )
    }

    Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
        when (screen) {
            Screen.Home -> {
                HomeScreen(
                    modifier = modifier,
                    summary = festivalSummary,
                    onNavigateToDonations = {
                        currentScreen = Screen.Donations
                    },
                    onNavigateToExpenses = {
                        currentScreen = Screen.Expenses
                    },
                    onNavigateToMembers = {
                        currentScreen = Screen.Members
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
                    initialDonations = donationsList,
                    onBackClick = {
                        currentScreen = Screen.Home
                    },
                    onDonationListUpdated = { updatedList ->
                        donationsList.clear()
                        donationsList.addAll(updatedList)
                    }
                )
            }

            Screen.Expenses -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                ExpenseScreen(
                    modifier = modifier,
                    initialExpenses = expensesList,
                    onBackClick = {
                        currentScreen = Screen.Home
                    },
                    onExpenseListUpdated = { updatedList ->
                        expensesList.clear()
                        expensesList.addAll(updatedList)
                    }
                )
            }

            Screen.Members -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                MemberScreen(
                    modifier = modifier,
                    initialMembers = membersList,
                    onBackClick = {
                        currentScreen = Screen.Home
                    },
                    onMemberListUpdated = { updatedList ->
                        membersList.clear()
                        membersList.addAll(updatedList)
                    }
                )
            }

            Screen.Karyakram -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                KaryakramScreen(
                    modifier = modifier,
                    initialKaryakramList = karyakramList,
                    onBackClick = {
                        currentScreen = Screen.Home
                    },
                    onKaryakramListUpdated = { updatedList ->
                        karyakramList.clear()
                        karyakramList.addAll(updatedList)
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
                    initialAartiId = screen.aartiId,
                    onBackClick = {
                        currentScreen = Screen.Home
                    }
                )
            }
        }
    }
}
