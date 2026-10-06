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
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import com.shreeganesh.ganapatimanagement.model.getSampleDonations
import com.shreeganesh.ganapatimanagement.ui.screens.aarti.AartiScreen
import com.shreeganesh.ganapatimanagement.ui.screens.donation.DonationScreen
import com.shreeganesh.ganapatimanagement.ui.screens.home.HomeScreen

sealed interface Screen {
    object Home : Screen
    object Donations : Screen
    data class Aarti(val aartiId: String? = null) : Screen
}

@Composable
fun GanapatiApp(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // Shared in-memory list of donations
    val donationsList = remember { mutableStateListOf<Donation>().apply { addAll(getSampleDonations()) } }

    // Dynamically calculate summary stats based on current donations
    val currentTotalDonation = donationsList.sumOf { it.amount }
    val currentDonorCount = donationsList.size

    val festivalSummary = remember(currentTotalDonation, currentDonorCount) {
        FestivalSummary(
            totalDonation = currentTotalDonation,
            donorCount = currentDonorCount
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
