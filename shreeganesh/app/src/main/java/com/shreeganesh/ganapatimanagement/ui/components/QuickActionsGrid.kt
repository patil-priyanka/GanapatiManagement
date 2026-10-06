package com.shreeganesh.ganapatimanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.QuickAction
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRed
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed

@Composable
fun defaultQuickActions(): List<QuickAction> {
    return listOf(
        QuickAction(
            id = "donations",
            title = "Donations",
            marathiTitle = "देणगी पावती",
            subtitle = "संकलन व पावती",
            icon = Icons.Default.CurrencyRupee,
            badge = "१४८ नोंद",
            accentColor = SaffronDeep,
            containerColor = Color(0xFFFFF3E0)
        ),
        QuickAction(
            id = "expenses",
            title = "Expenses",
            marathiTitle = "दैनिक खर्च",
            subtitle = "मंडप, डेकोरेशन, पूजा",
            icon = Icons.Default.AccountBalanceWallet,
            badge = "२४ बिले",
            accentColor = ExpenseRed,
            containerColor = Color(0xFFFFEBEE)
        ),
        QuickAction(
            id = "members",
            title = "Members",
            marathiTitle = "मंडळ कार्यकर्ते",
            subtitle = "पदाधिकारी व स्वयंसेवक",
            icon = Icons.Default.Groups,
            badge = "३२ सदस्य",
            accentColor = Color(0xFFE65100),
            containerColor = Color(0xFFFFF8E1)
        ),
        QuickAction(
            id = "tasks",
            title = "Tasks & Seva",
            marathiTitle = "उत्सव कामे",
            subtitle = "आरती, प्रसाद, स्वच्छता",
            icon = Icons.AutoMirrored.Filled.Assignment,
            badge = "५ प्रलंबित",
            accentColor = DonationGreen,
            containerColor = Color(0xFFE8F5E9)
        ),
        QuickAction(
            id = "events",
            title = "Events",
            marathiTitle = "कार्यक्रम व आरती",
            subtitle = "वेळापत्रक व स्पर्धा",
            icon = Icons.Default.Celebration,
            badge = "आज ४",
            accentColor = MarigoldGold,
            containerColor = Color(0xFFFFFDE7)
        ),
        QuickAction(
            id = "announcements",
            title = "Notices",
            marathiTitle = "सूचना फलक",
            subtitle = "मंडळ घोषणा व निमंत्रण",
            icon = Icons.Default.Campaign,
            badge = "नवीन",
            accentColor = VermilionRed,
            containerColor = Color(0xFFFFEBEE)
        )
    )
}

@Composable
fun QuickActionsSection(
    actions: List<QuickAction> = defaultQuickActions(),
    modifier: Modifier = Modifier,
    onActionClick: (QuickAction) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "उत्सव व्यवस्थापन (Quick Modules)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = "दैनंदिन सेवा आणि नियोजनाचे विभाग",
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3 rows x 2 columns grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (i in actions.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        action = actions[i],
                        modifier = Modifier.weight(1f),
                        onClick = { onActionClick(actions[i]) }
                    )
                    if (i + 1 < actions.size) {
                        QuickActionCard(
                            action = actions[i + 1],
                            modifier = Modifier.weight(1f),
                            onClick = { onActionClick(actions[i + 1]) }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    action: QuickAction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FestiveBorderLight))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Icon + Badge row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(action.containerColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.title,
                        tint = action.accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                action.badge?.let { badgeText ->
                    Surface(
                        color = action.accentColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = badgeText,
                            color = action.accentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = action.marathiTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = action.subtitle,
                fontSize = 11.sp,
                color = TextSecondaryLight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
