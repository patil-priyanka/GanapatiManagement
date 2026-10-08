package com.shreeganesh.ganapatimanagement.ui.screens.expense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
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
import com.shreeganesh.ganapatimanagement.model.Expense
import com.shreeganesh.ganapatimanagement.model.ExpenseCategory
import com.shreeganesh.ganapatimanagement.model.PaymentMode
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRed
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.AddPhotoAlternate
import com.shreeganesh.ganapatimanagement.ui.components.BillImagePreview
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseBottomSheet(
    nextVoucherNumber: String,
    onDismissRequest: () -> Unit,
    onExpenseAdded: (Expense) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var vendorName by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var paidBy by remember { mutableStateOf("खजिनदार") }
    var billNumber by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri.toString()
        }
    }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.MANDAP_DECORATION) }
    var selectedPaymentMode by remember { mutableStateOf(PaymentMode.CASH) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val quickAmounts = listOf("500", "1000", "2500", "5000", "10000")

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Surface(
                        color = ExpenseRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "व्हाऊचर क्र. #$nextVoucherNumber",
                            color = ExpenseRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "नवीन खर्च नोंदणी",
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

            // Error display
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

            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    errorMessage = null
                },
                label = { Text("खर्चाचे विवरण (शीर्षक) *") },
                placeholder = { Text("उदा. मंडप डेकोरेशन आगाऊ रक्कम") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = ExpenseRed)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ExpenseRed,
                    focusedLabelColor = ExpenseRed
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Vendor Name field
            OutlinedTextField(
                value = vendorName,
                onValueChange = {
                    vendorName = it
                    errorMessage = null
                },
                label = { Text("दुकानदार / व्हेंडर / व्यक्तीचे नाव *") },
                placeholder = { Text("उदा. प्रकाश डेकोरेटर्स / श्री समर्थ भंडार") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Store, contentDescription = null, tint = ExpenseRed)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ExpenseRed,
                    focusedLabelColor = ExpenseRed
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount field
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    if (it.all { char -> char.isDigit() || char == '.' }) {
                        amountText = it
                        errorMessage = null
                    }
                },
                label = { Text("खर्च रक्कम (₹) *") },
                placeholder = { Text("उदा. २५००") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = null, tint = ExpenseRed)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ExpenseRed,
                    focusedLabelColor = ExpenseRed
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
                quickAmounts.forEach { amt ->
                    Surface(
                        modifier = Modifier.clickable {
                            amountText = amt
                            errorMessage = null
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = if (amountText == amt) ExpenseRed else Color(0xFFFFEBEE),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                    ) {
                        Text(
                            text = "₹$amt",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (amountText == amt) Color.White else ExpenseRed,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text(
                text = "खर्च प्रकार (Category):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExpenseCategory.values().take(3).forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = "${cat.iconText} ${cat.marathiName}",
                                fontSize = 11.sp,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ExpenseRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExpenseCategory.values().drop(3).take(3).forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = "${cat.iconText} ${cat.marathiName}",
                                fontSize = 11.sp,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ExpenseRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

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
                PaymentMode.values().take(3).forEach { mode ->
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
                            selectedContainerColor = ExpenseRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Paid By Field
            OutlinedTextField(
                value = paidBy,
                onValueChange = { paidBy = it },
                label = { Text("खर्च करणारा कार्यकर्ता") },
                placeholder = { Text("उदा. अमित जोशी / सचिन पाटील") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = ExpenseRed)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bill number / Note
            OutlinedTextField(
                value = billNumber,
                onValueChange = { billNumber = it },
                label = { Text("बिल / पावती क्रमांक (ऐच्छिक)") },
                placeholder = { Text("उदा. INV-2026/04") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("टीप (ऐच्छिक)") },
                placeholder = { Text("उदा. आगाऊ रक्कम अदा केली") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Bill Image Upload Section
            Text(
                text = "बिल / पावती फोटो (Bill Image):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (selectedImageUri != null) {
                BillImagePreview(
                    imageUri = selectedImageUri,
                    title = if (title.isNotBlank()) title else "बिल फोटो",
                    height = 140,
                    onRemoveClick = { selectedImageUri = null }
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { photoPickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF7ED),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, SaffronPrimary.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "गॅलरीतून निवडा",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "गॅलरीतून बिल किंवा पावती फोटो निवडा",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    if (title.trim().isBlank()) {
                        errorMessage = "कृपया खर्चाचे विवरण प्रविष्ट करा"
                        return@Button
                    }
                    if (vendorName.trim().isBlank()) {
                        errorMessage = "कृपया दुकानदार / व्यक्तीचे नाव प्रविष्ट करा"
                        return@Button
                    }
                    val amountValue = amountText.toDoubleOrNull()
                    if (amountValue == null || amountValue <= 0) {
                        errorMessage = "कृपया वैध खर्च रक्कम प्रविष्ट करा"
                        return@Button
                    }

                    val newExpense = Expense(
                        id = "exp_${System.currentTimeMillis()}",
                        voucherNumber = nextVoucherNumber,
                        title = title.trim(),
                        vendorName = vendorName.trim(),
                        category = selectedCategory,
                        amount = amountValue,
                        paymentMode = selectedPaymentMode,
                        date = "०६ ऑक्टो २०२६",
                        time = "०३:१५ PM",
                        paidBy = if (paidBy.trim().isNotBlank()) paidBy.trim() else "खजिनदार",
                        billNumber = billNumber.trim(),
                        note = note.trim(),
                        imageUri = selectedImageUri
                    )
                    onExpenseAdded(newExpense)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed)
            ) {
                Text(
                    text = "खर्च जतन करा व व्हाऊचर बनवा",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
