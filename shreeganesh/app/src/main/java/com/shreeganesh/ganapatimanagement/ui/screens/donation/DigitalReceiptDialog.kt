package com.shreeganesh.ganapatimanagement.ui.screens.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shreeganesh.ganapatimanagement.model.Donation
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionDeep
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DigitalReceiptDialog(
    donation: Donation,
    onDismissRequest: () -> Unit,
    onShareReceipt: (Donation) -> Unit = {}
) {
    val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(
                    listOf(MarigoldGold, SaffronPrimary, FestiveBorderLight)
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button top right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SaffronDeep.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "॥ श्री गणेशाय नमः ॥",
                            color = SaffronDeep,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "बंद करा",
                            tint = TextSecondaryLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mandal Header
                Text(
                    text = "श्री सार्वजनिक गणेशोत्सव मंडळ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = VermilionDeep,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "शिवाजी नगर, पुणे • नोंदणी क्र. महा/१२४५/१९९८",
                    fontSize = 11.sp,
                    color = TextSecondaryLight,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Receipt Title Pill
                Surface(
                    color = SaffronPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "अधिकृत देणगी पावती",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Receipt No and Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "पावती क्र: #${donation.receiptNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronDeep
                    )
                    Text(
                        text = "${donation.date} ${donation.time}",
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = FestiveBorderLight,
                    thickness = 1.dp
                )

                // Details Table
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReceiptFieldRow(label = "देणगीदाराचे नाव", value = donation.donorName, isBold = true)
                    if (donation.phoneNumber.isNotBlank()) {
                        ReceiptFieldRow(label = "मोबाईल नंबर", value = donation.phoneNumber)
                    }
                    if (donation.address.isNotBlank()) {
                        ReceiptFieldRow(label = "पत्ता", value = donation.address)
                    }
                    ReceiptFieldRow(label = "देणगी प्रकार", value = donation.category.marathiName)
                    ReceiptFieldRow(
                        label = "भरणा पद्धत",
                        value = "${donation.paymentMode.iconText} ${donation.paymentMode.marathiName}"
                    )
                    if (donation.note.isNotBlank()) {
                        ReceiptFieldRow(label = "टीप / उद्देश", value = donation.note)
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = FestiveBorderLight,
                    thickness = 1.dp
                )

                // Large Amount Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF1F8E9),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DonationGreen.copy(alpha = 0.5f)))
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "प्राप्त देणगी रक्कम",
                            fontSize = 11.sp,
                            color = DonationGreen,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹ ${currencyFormatter.format(donation.amount.toLong())}/-",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = DonationGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stamp & Mandal Signature
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "🌺 बाप्पाची कृपा सदैव राहो!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronDeep
                        )
                        Text(
                            text = "आपल्या सहकार्याबद्दल धन्यवाद.",
                            fontSize = 10.sp,
                            color = TextSecondaryLight
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = VermilionRed.copy(alpha = 0.08f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, VermilionRed.copy(alpha = 0.4f)),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "मुद्रा\nSEAL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VermilionRed,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "खजिनदार / अध्यक्ष",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Actions: Share & Done
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SaffronDeep),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronDeep)
                    ) {
                        Text(
                            text = "बंद करा",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onShareReceipt(donation) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "शेअर करा",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "पावती शेअर करा",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptFieldRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondaryLight,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = TextPrimaryLight,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f)
        )
    }
}
