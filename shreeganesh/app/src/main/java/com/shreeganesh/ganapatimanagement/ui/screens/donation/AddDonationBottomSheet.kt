package com.shreeganesh.ganapatimanagement.ui.screens.donation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.Donation
import com.shreeganesh.ganapatimanagement.model.DonationCategory
import com.shreeganesh.ganapatimanagement.model.PaymentMode
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDonationBottomSheet(
    nextReceiptNumber: String,
    onDismissRequest: () -> Unit,
    onDonationAdded: (Donation) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var donorName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedPaymentMode by remember { mutableStateOf(PaymentMode.CASH) }
    var selectedCategory by remember { mutableStateOf(DonationCategory.GENERAL) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val quickAmounts = listOf("१०१", "२५१", "५०१", "१००१", "२१००", "५००१", "११०००")

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SaffronDeep.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "पावती क्र. #$nextReceiptNumber",
                                color = SaffronDeep,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "नवीन देणगी नोंदणी",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimaryLight
                    )
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "रद्द करा",
                        tint = TextSecondaryLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error display if validation fails
            errorMessage?.let { error ->
                Surface(
                    color = VermilionRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "⚠️ $error",
                        color = VermilionRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            // Donor Name Field
            OutlinedTextField(
                value = donorName,
                onValueChange = {
                    donorName = it
                    errorMessage = null
                },
                label = { Text("देणगीदाराचे नाव *") },
                placeholder = { Text("उदा. रमेश बाळकृष्ण पाटील") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = SaffronPrimary)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaffronPrimary,
                    focusedLabelColor = SaffronPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Number Field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                        phoneNumber = it
                    }
                },
                label = { Text("मोबाईल नंबर (ऐच्छिक)") },
                placeholder = { Text("१० अंकी मोबाईल नंबर") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = SaffronPrimary)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaffronPrimary,
                    focusedLabelColor = SaffronPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount Field
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    if (it.all { char -> char.isDigit() || char == '.' }) {
                        amountText = it
                        errorMessage = null
                    }
                },
                label = { Text("देणगी रक्कम (₹) *") },
                placeholder = { Text("उदा. ५००१") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = null, tint = DonationGreen)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DonationGreen,
                    focusedLabelColor = DonationGreen
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Preset Amount Chips
            Text(
                text = "पटकन रक्कम निवडा:",
                fontSize = 11.sp,
                color = TextSecondaryLight,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val englishNumbers = listOf("101", "251", "501", "1001", "2100", "5001")
                englishNumbers.forEachIndexed { idx, amt ->
                    Surface(
                        modifier = Modifier.clickable {
                            amountText = amt
                            errorMessage = null
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = if (amountText == amt) SaffronPrimary else Color(0xFFFFF3E0),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                    ) {
                        Text(
                            text = "₹$amt",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (amountText == amt) Color.White else SaffronDeep,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Mode Selection
            Text(
                text = "भरणा पद्धत (Payment Mode):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMode.values().forEach { mode ->
                    FilterChip(
                        selected = selectedPaymentMode == mode,
                        onClick = { selectedPaymentMode = mode },
                        label = {
                            Text(
                                text = "${mode.iconText} ${mode.marathiName}",
                                fontSize = 11.sp,
                                fontWeight = if (selectedPaymentMode == mode) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Selection
            Text(
                text = "देणगी कारण / प्रकार (Category):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DonationCategory.values().take(3).forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = cat.marathiName,
                                fontSize = 11.sp,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronDeep,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DonationCategory.values().drop(3).forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = cat.marathiName,
                                fontSize = 11.sp,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronDeep,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Address / Note Field
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("पत्ता / परिसर (ऐच्छिक)") },
                placeholder = { Text("उदा. शुक्रवार पेठ, पुणे") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("टीप (ऐच्छिक)") },
                placeholder = { Text("उदा. नैवेद्य सेवा / प्रायोजक") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    if (donorName.trim().isBlank()) {
                        errorMessage = "कृपया देणगीदाराचे नाव प्रविष्ट करा"
                        return@Button
                    }
                    val amountValue = amountText.toDoubleOrNull()
                    if (amountValue == null || amountValue <= 0) {
                        errorMessage = "कृपया वैध देणगी रक्कम प्रविष्ट करा"
                        return@Button
                    }

                    val newDonation = Donation(
                        id = "d_${System.currentTimeMillis()}",
                        receiptNumber = nextReceiptNumber,
                        donorName = donorName.trim(),
                        phoneNumber = phoneNumber.trim(),
                        amount = amountValue,
                        paymentMode = selectedPaymentMode,
                        category = selectedCategory,
                        date = "०६ ऑक्टो २०२६",
                        time = "१२:५० PM",
                        address = address.trim(),
                        note = note.trim()
                    )
                    onDonationAdded(newDonation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text(
                    text = "पावती तयार करा व जतन करा",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
