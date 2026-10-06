package com.shreeganesh.ganapatimanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronContainer
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight

sealed class DashboardTab(
    val route: String,
    val title: String,
    val marathiTitle: String,
    val icon: ImageVector
) {
    object Home : DashboardTab("home", "Home", "मुख्य", Icons.Default.Home)
    object Finance : DashboardTab("finance", "Finance", "हिशोब", Icons.Default.AccountBalanceWallet)
    object Tasks : DashboardTab("tasks", "Tasks", "कामे", Icons.AutoMirrored.Filled.Assignment)
    object Events : DashboardTab("events", "Events", "कार्यक्रम", Icons.Default.Celebration)
    object Committee : DashboardTab("committee", "Mandal", "मंडळ", Icons.Default.Groups)
}

@Composable
fun FestiveBottomNavigationBar(
    currentTab: DashboardTab = DashboardTab.Home,
    modifier: Modifier = Modifier,
    onTabSelected: (DashboardTab) -> Unit = {}
) {
    val tabs = listOf(
        DashboardTab.Home,
        DashboardTab.Finance,
        DashboardTab.Tasks,
        DashboardTab.Events,
        DashboardTab.Committee
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.height(68.dp)
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab.route == tab.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = tab.marathiTitle,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronDeep,
                        selectedTextColor = SaffronDeep,
                        indicatorColor = SaffronContainer,
                        unselectedIconColor = TextSecondaryLight,
                        unselectedTextColor = TextSecondaryLight
                    )
                )
            }
        }
    }
}
