package com.shreeganesh.ganapatimanagement.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import com.shreeganesh.ganapatimanagement.ui.components.AnnouncementBanner
import com.shreeganesh.ganapatimanagement.ui.components.DashboardTab
import com.shreeganesh.ganapatimanagement.ui.components.FestivalTopHeader
import com.shreeganesh.ganapatimanagement.ui.components.FestiveBottomNavigationBar
import com.shreeganesh.ganapatimanagement.ui.components.FinancialSummaryCard
import com.shreeganesh.ganapatimanagement.ui.components.QuickActionsSection
import com.shreeganesh.ganapatimanagement.ui.components.RecentActivitySection
import com.shreeganesh.ganapatimanagement.ui.components.TodayScheduleSection
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBackgroundLight
import com.shreeganesh.ganapatimanagement.ui.theme.GanapatiManagementTheme
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    summary: FestivalSummary = FestivalSummary(),
    onNavigateToDonations: () -> Unit = {},
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToMembers: () -> Unit = {},
    onNavigateToTasks: () -> Unit = {},
    onNavigateToEvents: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf<DashboardTab>(DashboardTab.Home) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FestiveBackgroundLight,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            FestiveBottomNavigationBar(
                currentTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    showMessage("${tab.marathiTitle} विभाग निवडला")
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showMessage("नवीन देणगी किंवा खर्च नोंदवा")
                },
                containerColor = SaffronPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "नोंद करा",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "नोंद करा",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Auspicious Festive Top Header
            item {
                FestivalTopHeader(
                    summary = summary,
                    onNotificationClick = {
                        showMessage("नवीन सूचना: ३ नवीन संदेश उपलब्ध")
                    },
                    onInfoClick = {
                        showMessage("${summary.mandalName} • माहिती")
                    }
                )
            }

            // 2. Financial Summary Hero Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    FinancialSummaryCard(
                        summary = summary,
                        onAddDonationClick = {
                            onNavigateToDonations()
                            showMessage("देणगी नोंद फॉर्म")
                        },
                        onAddExpenseClick = {
                            onNavigateToExpenses()
                            showMessage("खर्च नोंद फॉर्म")
                        },
                        onViewFinanceClick = {
                            showMessage("हिशोब तपशील")
                        }
                    )
                }
            }

            // 3. Important Announcement Banner
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AnnouncementBanner(
                        onClick = {
                            showMessage("मंडळ सूचना फलक उघडला")
                        }
                    )
                }
            }

            // 4. Quick Action Modules (6 cards)
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    QuickActionsSection(
                        onActionClick = { action ->
                            when (action.id) {
                                "donations" -> onNavigateToDonations()
                                "expenses" -> onNavigateToExpenses()
                                "members" -> onNavigateToMembers()
                                "tasks" -> onNavigateToTasks()
                                "events" -> onNavigateToEvents()
                                else -> {}
                            }
                            showMessage("${action.marathiTitle} विभाग उघडत आहे...")
                        }
                    )
                }
            }

            // 5. Today's Aarti & Schedule Timeline
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TodayScheduleSection(
                        onViewAllClick = {
                            onNavigateToEvents()
                            showMessage("सर्व कार्यक्रम वेळापत्रक")
                        },
                        onEventClick = { event ->
                            showMessage("${event.marathiTitle} • वेळ: ${event.time}")
                        }
                    )
                }
            }

            // 6. Recent Activity & Transactions
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    RecentActivitySection(
                        onViewAllClick = {
                            showMessage("सर्व व्यवहार व नोंदींची यादी")
                        },
                        onItemClick = { act ->
                            showMessage("${act.title}: ₹${act.amount?.toInt() ?: 0}")
                        }
                    )
                }
            }

            // Auspicious Footer blessing
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "॥ गणपती बाप्पा मोरया • मंगलमूर्ती मोरया ॥",
                        color = SaffronDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "श्री गणेश उत्सव व्यवस्थापन २०२६",
                        color = Color(0xFF9E826C),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    GanapatiManagementTheme {
        HomeScreen()
    }
}
