package com.shreeganesh.ganapatimanagement.model

import androidx.compose.ui.graphics.Color

enum class PaymentMode(val displayName: String, val marathiName: String, val iconText: String) {
    CASH("Cash", "रोख", "💵"),
    UPI("Online UPI", "ऑनलाईन UPI", "📱"),
    CHEQUE("Cheque", "धनादेश", "📝"),
    BANK_TRANSFER("Bank Transfer", "बँक ट्रान्सफर", "🏛️")
}

enum class DonationCategory(val displayName: String, val marathiName: String) {
    GENERAL("General Fund", "सर्वसाधारण देणगी"),
    MAHAPRASAD("Mahaprasad", "महाप्रसाद अन्नदान"),
    AARTI("Aarti & Pooja", "आरती व पूजा"),
    DECORATION("Mandap & Decoration", "मंडप व सजावट"),
    VISARJAN("Visarjan Seva", "विसर्जन मिरवणूक")
}

data class Donation(
    val id: String,
    val receiptNumber: String,
    val donorName: String,
    val phoneNumber: String = "",
    val amount: Double,
    val paymentMode: PaymentMode = PaymentMode.CASH,
    val category: DonationCategory = DonationCategory.GENERAL,
    val date: String,
    val time: String,
    val address: String = "",
    val note: String = ""
) {
    val formattedReceiptNumber: String get() = "पावती क्र. #$receiptNumber"
}

data class DonationStats(
    val totalAmount: Double,
    val totalDonors: Int,
    val todayAmount: Double,
    val cashAmount: Double,
    val upiAmount: Double
) {
    val upiPercentage: Float
        get() = if (totalAmount > 0) (upiAmount / totalAmount).toFloat().coerceIn(0f, 1f) else 0f
}
