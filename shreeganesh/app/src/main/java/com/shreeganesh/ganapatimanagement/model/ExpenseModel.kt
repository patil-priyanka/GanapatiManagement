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
