package com.shreeganesh.ganapatimanagement.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FestivalSummary(
    val festivalTitle: String = "श्री गणेश उत्सव २०२६",
    val mandalName: String = "श्री सार्वजनिक गणेशोत्सव मंडळ",
    val location: String = "शिवाजी नगर • स्थापना १९९८",
    val motto: String = "॥ श्री गणेशाय नमः ॥",
    val currentDay: Int = 3,
    val totalDays: Int = 10,
    val totalDonation: Double = 125500.0,
    val totalExpense: Double = 42350.0,
    val targetBudget: Double = 150000.0,
    val donorCount: Int = 148,
    val volunteerCount: Int = 32
) {
    val balance: Double get() = totalDonation - totalExpense
    val progressPercent: Int get() = ((totalDonation / targetBudget) * 100).toInt().coerceIn(0, 100)
    val remainingBudget: Double get() = (targetBudget - totalDonation).coerceAtLeast(0.0)
}

data class QuickAction(
    val id: String,
    val title: String,
    val marathiTitle: String,
    val subtitle: String,
    val icon: ImageVector,
    val badge: String? = null,
    val accentColor: Color,
    val containerColor: Color
)

data class AartiEvent(
    val id: String,
    val title: String,
    val marathiTitle: String,
    val time: String,
    val location: String = "मुख्य मंडप",
    val isNext: Boolean = false,
    val isCompleted: Boolean = false,
    val prasadDetails: String? = null,
    val host: String? = null
)

data class RecentActivity(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double? = null,
    val isDonation: Boolean = true,
    val category: String,
    val timeAgo: String
)

data class AnnouncementItem(
    val id: String,
    val title: String,
    val message: String,
    val tag: String = "महत्त्वाचे",
    val timeAgo: String = "१ तास"
)
