package com.shreeganesh.ganapatimanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.RecentActivity
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreenContainer
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRed
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRedContainer
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import java.text.NumberFormat
import java.util.Locale

@Composable
fun defaultRecentActivities(): List<RecentActivity> {
    return listOf(
        RecentActivity(
            id = "act_1",
            title = "रमेश बाळकृष्ण पाटील",
            subtitle = "पावती क्र. #१०४ • ऑनलाईन UPI",
            amount = 5001.0,
            isDonation = true,
            category = "देणगी",
            timeAgo = "१० मि. पूर्वी"
        ),
        RecentActivity(
            id = "act_2",
            title = "मंडप विद्युत रोषणाई आगाऊ रक्कम",
            subtitle = "प्रकाश डेकोरेटर्स • व्हाऊचर #१८",
            amount = 12500.0,
            isDonation = false,
            category = "खर्च",
            timeAgo = "४५ मि. पूर्वी"
        ),
        RecentActivity(
            id = "act_3",
            title = "साई समर्थ इंडस्ट्रीज",
            subtitle = "पावती क्र. #१०३ • रोख देणगी",
            amount = 21000.0,
            isDonation = true,
            category = "देणगी",
            timeAgo = "२ तास पूर्वी"
        ),
        RecentActivity(
            id = "act_4",
            title = "पूजा साहित्य व दुर्वा-हार खरेदी",
            subtitle = "श्री गुरुदेव पूजा भंडार • रोख",
            amount = 2450.0,
            isDonation = false,
            category = "खर्च",
            timeAgo = "४ तास पूर्वी"
        )
    )
}

@Composable
fun RecentActivitySection(
    activities: List<RecentActivity> = defaultRecentActivities(),
    modifier: Modifier = Modifier,
    onViewAllClick: () -> Unit = {},
    onItemClick: (RecentActivity) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("सर्व") }
    val filters = listOf("सर्व", "देणगी", "खर्च")

    val filteredActivities = when (selectedFilter) {
        "देणगी" -> activities.filter { it.isDonation }
        "खर्च" -> activities.filter { !it.isDonation }
        else -> activities
    }

    val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "अलीकडील नोंदी व व्यवहार (Recent)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = "नवीनतम देणगी पावती व दैनंदिन खर्च",
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )
            }

            Surface(
                modifier = Modifier.clickable { onViewAllClick() },
                color = SaffronPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "सर्व नोंदी ›",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronDeep,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Activity List
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FestiveBorderLight))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                filteredActivities.forEachIndexed { index, item ->
                    ActivityRowItem(
                        item = item,
                        currencyFormatter = currencyFormatter,
                        onClick = { onItemClick(item) }
                    )
                    if (index < filteredActivities.size - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(1.dp)
                                .background(Color(0xFFF4ECE4))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityRowItem(
    item: RecentActivity,
    currencyFormatter: NumberFormat,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon (Inflow green vs Outflow red)
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (item.isDonation) DonationGreenContainer else ExpenseRedContainer,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (item.isDonation) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = if (item.isDonation) DonationGreen else ExpenseRed,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${item.subtitle} • ${item.timeAgo}",
                fontSize = 11.sp,
                color = TextSecondaryLight
            )
        }

        // Amount Display
        item.amount?.let { amt ->
            Text(
                text = "${if (item.isDonation) "+" else "-"} ₹${currencyFormatter.format(amt.toLong())}",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (item.isDonation) DonationGreen else ExpenseRed
            )
        }
    }
}
