package com.shreeganesh.ganapatimanagement.model

enum class ExpenseCategory(val displayName: String, val marathiName: String, val iconText: String) {
    MANDAP_DECORATION("Mandap & Decoration", "मंडप व सजावट", "🎪"),
    LIGHTING("Lighting & Power", "विद्युत रोषणाई", "💡"),
    SOUND_SYSTEM("Sound System", "ध्वनी व्यवस्था", "🔊"),
    POOJA_AARTI("Pooja & Flowers", "पूजा व फुले-हार", "🪔"),
    MAHAPRASAD("Mahaprasad & Food", "महाप्रसाद किराणा", "🍛"),
    DHOL_TASHA("Dhol Tasha & Music", "ढोल ताशा व वाद्ये", "🎺"),
    TRANSPORT("Transport & Fuel", "वाहतूक व टेम्पो", "🚗"),
    PRINTING_PUBLICITY("Flex & Banners", "बॅनर व प्रसिद्धी", "📋"),
    OTHER("Miscellaneous", "इतर किरकोळ खर्च", "📦")
}

data class Expense(
    val id: String,
    val voucherNumber: String,
    val title: String,
    val vendorName: String,
    val category: ExpenseCategory,
    val amount: Double,
    val paymentMode: PaymentMode = PaymentMode.CASH,
    val date: String,
    val time: String,
    val paidBy: String = "खजिनदार",
    val billNumber: String = "",
    val note: String = "",
    val isPending: Boolean = false,
    val pendingAmount: Double = 0.0,
    val imageUri: String? = null
) {
    val formattedVoucherNumber: String get() = "व्हाऊचर क्र. #$voucherNumber"
}

data class ExpenseStats(
    val totalExpense: Double,
    val totalVouchers: Int,
    val todayExpense: Double,
    val cashExpense: Double,
    val onlineExpense: Double,
    val pendingDues: Double
)

fun getSampleExpenses(): List<Expense> {
    return listOf(
        Expense(
            id = "exp_1",
            voucherNumber = "०१८",
            title = "मंडप उभारणी व वॉटरप्रूफिंग आगाऊ रक्कम",
            vendorName = "प्रकाश मंडप डेकोरेटर्स",
            category = ExpenseCategory.MANDAP_DECORATION,
            amount = 12500.0,
            paymentMode = PaymentMode.UPI,
            date = "०६ ऑक्टो २०२६",
            time = "११:३० AM",
            paidBy = "अमित जोशी (खजिनदार)",
            billNumber = "PMD-241",
            note = "मंडप स्टेज व मुख्य कमान उभारणी आगाऊ उचल"
        ),
        Expense(
            id = "exp_2",
            voucherNumber = "०१७",
            title = "जनरेटर व सिरीयल लाईट विद्युत रोषणाई",
            vendorName = "ओम साई इलेक्ट्रीकल्स",
            category = ExpenseCategory.LIGHTING,
            amount = 8500.0,
            paymentMode = PaymentMode.UPI,
            date = "०६ ऑक्टो २०२६",
            time = "१०:०० AM",
            paidBy = "सचिन पाटील",
            billNumber = "OSE-88",
            note = "१० दिवसांचे जनरेटर भाडे व लाईट फिटिंग"
        ),
        Expense(
            id = "exp_3",
            voucherNumber = "०१६",
            title = "दैनिक महाआरती साऊंड सिस्टीम व माईक",
            vendorName = "स्वरझंकार साऊंड्स",
            category = ExpenseCategory.SOUND_SYSTEM,
            amount = 6000.0,
            paymentMode = PaymentMode.CASH,
            date = "०५ ऑक्टो २०२६",
            time = "०८:०० PM",
            paidBy = "रोहित कुलकर्णी",
            billNumber = "SZ-104",
            note = "आरती व भजन साऊंड व्यवस्था"
        ),
        Expense(
            id = "exp_4",
            voucherNumber = "०१५",
            title = "महाप्रसाद किराणा, तेल व मसाले खरेदी",
            vendorName = "श्री गणेश सुपर मार्केट",
            category = ExpenseCategory.MAHAPRASAD,
            amount = 7850.0,
            paymentMode = PaymentMode.UPI,
            date = "०५ ऑक्टो २०२६",
            time = "०४:४५ PM",
            paidBy = "विकास शिंदे",
            billNumber = "GSM-902",
            note = "५०० भाविकांसाठी महाप्रसाद भोजन साहित्य"
        ),
        Expense(
            id = "exp_5",
            voucherNumber = "०१४",
            title = "पूजा साहित्य, दुर्वा, गुलाब व हार खरेदी",
            vendorName = "श्री समर्थ पूजा भंडार",
            category = ExpenseCategory.POOJA_AARTI,
            amount = 2450.0,
            paymentMode = PaymentMode.CASH,
            date = "०५ ऑक्टो २०२६",
            time = "०९:१५ AM",
            paidBy = "प्रशांत मोरे",
            note = "सकाळ-संध्याकाळ पूजेसाठी फुले व हार"
        ),
        Expense(
            id = "exp_6",
            voucherNumber = "०१३",
            title = "गणेश आगमन मिरवणूक ढोल ताशा पथक",
            vendorName = "शिवमुद्रा ढोल ताशा पथक",
            category = ExpenseCategory.DHOL_TASHA,
            amount = 5000.0,
            paymentMode = PaymentMode.CHEQUE,
            date = "०४ ऑक्टो २०२६",
            time = "०३:३० PM",
            paidBy = "अमित जोशी (खजिनदार)",
            billNumber = "CHQ-77412",
            note = "आगमन मिरवणूक मानधन आगाऊ धनादेश"
        )
    )
}
