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

fun getSampleDonations(): List<Donation> {
    return listOf(
        Donation(
            id = "d1",
            receiptNumber = "१०४",
            donorName = "रमेश बाळकृष्ण पाटील",
            phoneNumber = "9822012345",
            amount = 5001.0,
            paymentMode = PaymentMode.UPI,
            category = DonationCategory.GENERAL,
            date = "०६ ऑक्टो २०२६",
            time = "१०:१५ AM",
            address = "शिवाजी नगर, पुणे",
            note = "कुटुंबाच्या वतीने सदिच्छा देणगी"
        ),
        Donation(
            id = "d2",
            receiptNumber = "१०३",
            donorName = "साई समर्थ कन्स्ट्रक्शन्स (श्री. शिंदे)",
            phoneNumber = "9423114455",
            amount = 21000.0,
            paymentMode = PaymentMode.UPI,
            category = DonationCategory.MAHAPRASAD,
            date = "०६ ऑक्टो २०२६",
            time = "०९:३० AM",
            address = "डेक्कन, पुणे",
            note = "महाप्रसाद अन्नदान प्रायोजकत्व"
        ),
        Donation(
            id = "d3",
            receiptNumber = "१०२",
            donorName = "सौ. सुनीता गजानन जोशी",
            phoneNumber = "9890234567",
            amount = 2100.0,
            paymentMode = PaymentMode.CASH,
            category = DonationCategory.AARTI,
            date = "०५ ऑक्टो २०२६",
            time = "०८:४५ PM",
            address = "शनिवार पेठ, पुणे",
            note = "संध्या आरती नैवेद्य सेवा"
        ),
        Donation(
            id = "d4",
            receiptNumber = "१०१",
            donorName = "विजय यशवंत कुलकर्णी",
            phoneNumber = "9860456789",
            amount = 11000.0,
            paymentMode = PaymentMode.CHEQUE,
            category = DonationCategory.DECORATION,
            date = "०५ ऑक्टो २०२६",
            time = "०६:२० PM",
            address = "कोथरूड, पुणे",
            note = "मंडप विद्युत रोषणाई मदत"
        ),
        Donation(
            id = "d5",
            receiptNumber = "१००",
            donorName = "अमित प्रकाश देशमुख",
            phoneNumber = "9765123980",
            amount = 1001.0,
            paymentMode = PaymentMode.CASH,
            category = DonationCategory.GENERAL,
            date = "०५ ऑक्टो २०२६",
            time = "०४:०० PM",
            address = "सदाशिव पेठ, पुणे"
        ),
        Donation(
            id = "d6",
            receiptNumber = "०९९",
            donorName = "मयूर अशोक जाधव",
            phoneNumber = "9552123456",
            amount = 501.0,
            paymentMode = PaymentMode.UPI,
            category = DonationCategory.GENERAL,
            date = "०४ ऑक्टो २०२६",
            time = "११:१० AM",
            address = "पुणे"
        ),
        Donation(
            id = "d7",
            receiptNumber = "०९८",
            donorName = "गणेश कृपा ज्वेलर्स",
            phoneNumber = "9823098765",
            amount = 25000.0,
            paymentMode = PaymentMode.UPI,
            category = DonationCategory.MAHAPRASAD,
            date = "०४ ऑक्टो २०२६",
            time = "१०:०० AM",
            address = "लक्ष्मी रोड, पुणे",
            note = "अन्नदान महाप्रसाद मुख्य प्रायोजक"
        )
    )
}
