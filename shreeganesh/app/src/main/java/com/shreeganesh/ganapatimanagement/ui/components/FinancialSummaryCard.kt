package com.shreeganesh.ganapatimanagement.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreenContainer
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRed
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRedContainer
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinancialSummaryCard(
    summary: FestivalSummary,
    modifier: Modifier = Modifier,
    onAddDonationClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {},
    onViewFinanceClick: () -> Unit = {}
) {
    val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
    val animatedProgress by animateFloatAsState(
        targetValue = (summary.totalDonation / summary.targetBudget).toFloat().coerceIn(0f, 1f),
        label = "budgetProgress"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewFinanceClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                colors = listOf(FestiveBorderLight, SaffronLight.copy(alpha = 0.4f))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Card Header: Title & Total Net Fund
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "हिशोब व निधी आढावा",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• उत्सव २०२६",
                            fontSize = 12.sp,
                            color = TextSecondaryLight
                        )
                    }
                    Text(
                        text = "एकूण शिल्लक निधी (Net Balance)",
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )
                }

                Surface(
                    color = MarigoldGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "विस्तार पहा ›",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SaffronDeep,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Large Net Balance Display
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "₹",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronDeep
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = currencyFormatter.format(summary.balance.toLong()),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryLight,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.weight(1f))

                // Donor count pill
                Surface(
                    color = Color(0xFFF3ECE4),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "🤝 ${summary.donorCount} देणगीदार",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondaryLight,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Income (Donation) vs Expense Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Inflow: Donations
                Surface(
                    modifier = Modifier.weight(1f),
                    color = DonationGreenContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(DonationGreen.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(DonationGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "जमा",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "एकूण देणगी (जमा)",
                                fontSize = 11.sp,
                                color = DonationGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "₹${currencyFormatter.format(summary.totalDonation.toLong())}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                        }
                    }
                }

                // Outflow: Expenses
                Surface(
                    modifier = Modifier.weight(1f),
                    color = ExpenseRedContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(ExpenseRed.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(ExpenseRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "खर्च",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "एकूण खर्च (नावे)",
                                fontSize = 11.sp,
                                color = ExpenseRed,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "₹${currencyFormatter.format(summary.totalExpense.toLong())}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Budget Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "उत्सव संकलन उद्दिष्ट (Target Budget)",
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                    Text(
                        text = "${summary.progressPercent}% पूर्ण (उद्दिष्ट ₹${currencyFormatter.format(summary.targetBudget.toLong())})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronDeep
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SaffronPrimary,
                    trackColor = Color(0xFFF0E5D8)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Entry Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAddDonationClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "देणगी नोंदवा",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onAddExpenseClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, ExpenseRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ExpenseRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "खर्च नोंदवा",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
