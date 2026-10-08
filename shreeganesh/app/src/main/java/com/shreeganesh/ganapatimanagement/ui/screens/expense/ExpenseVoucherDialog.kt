package com.shreeganesh.ganapatimanagement.ui.screens.expense

import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shreeganesh.ganapatimanagement.model.Expense
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRed
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRedContainer
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
fun ExpenseVoucherDialog(
    expense: Expense,
    onDismissRequest: () -> Unit,
    onShareVoucher: (Expense) -> Unit = {}
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
                    listOf(VermilionRed, SaffronPrimary, FestiveBorderLight)
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button top right & auspicious badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = VermilionRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "॥ खर्च व्हाऊचर पावती ॥",
                            color = VermilionDeep,
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
                    text = "हिशोब व खर्च विभाग • उत्सव २०२६",
                    fontSize = 11.sp,
                    color = TextSecondaryLight,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Voucher Title Pill
                Surface(
                    color = ExpenseRed,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "पेमेंट व्हाऊचर (Payment Voucher)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Voucher No and Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "व्हाऊचर क्र: #${expense.voucherNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ExpenseRed
                    )
                    Text(
                        text = "${expense.date} ${expense.time}",
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = FestiveBorderLight,
                    thickness = 1.dp
                )

                // Details Rows
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VoucherFieldRow(label = "खर्चाचे विवरण", value = expense.title, isBold = true)
                    VoucherFieldRow(label = "दुकानदार / व्हेंडर", value = expense.vendorName)
                    VoucherFieldRow(label = "खर्च प्रकार", value = "${expense.category.iconText} ${expense.category.marathiName}")
                    VoucherFieldRow(
                        label = "भरणा पद्धत",
                        value = "${expense.paymentMode.iconText} ${expense.paymentMode.marathiName}"
                    )
                    VoucherFieldRow(label = "खर्च करणारा कार्यकर्ता", value = expense.paidBy)
                    if (expense.billNumber.isNotBlank()) {
                        VoucherFieldRow(label = "बिल / व्हाऊचर संदर्भ", value = expense.billNumber)
                    }
                    if (expense.note.isNotBlank()) {
                        VoucherFieldRow(label = "टीप", value = expense.note)
                    }
                }

                // Attached Bill / Receipt Photo Preview if present
                if (expense.imageUri != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "📎 जोडलेले बिल / पावती (Attached Bill):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondaryLight,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    com.shreeganesh.ganapatimanagement.ui.components.BillImagePreview(
                        imageUri = expense.imageUri,
                        title = expense.title,
                        height = 140
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = FestiveBorderLight,
                    thickness = 1.dp
                )

                // Large Amount Box (Expense Red)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = ExpenseRedContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ExpenseRed.copy(alpha = 0.5f)))
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "एकूण खर्ची पडलेली रक्कम",
                            fontSize = 11.sp,
                            color = ExpenseRed,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹ ${currencyFormatter.format(expense.amount.toLong())}/-",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = ExpenseRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Signatures
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "स्वीकारणारा / व्हेंडर",
                            fontSize = 10.sp,
                            color = TextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "स्वाक्षरी / पावती जोडली",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
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
                                    text = "मंजूर\nPAID",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VermilionRed,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "खजिनदार स्वाक्षरी",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Actions: Close & Share
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
                        border = androidx.compose.foundation.BorderStroke(1.dp, ExpenseRed),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ExpenseRed)
                    ) {
                        Text(
                            text = "बंद करा",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onShareVoucher(expense) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "शेअर करा",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "व्हाऊचर शेअर करा",
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
private fun VoucherFieldRow(label: String, value: String, isBold: Boolean = false) {
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
