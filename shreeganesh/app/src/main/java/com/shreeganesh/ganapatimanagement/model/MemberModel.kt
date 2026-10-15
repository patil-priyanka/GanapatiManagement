package com.shreeganesh.ganapatimanagement.model

enum class MemberRole(
    val title: String,
    val marathiTitle: String,
    val iconEmoji: String,
    val isCoreBear: Boolean = false
) {
    PRESIDENT("President", "अध्यक्ष", "🚩", true),
    VICE_PRESIDENT("Vice President", "उपाध्यक्ष", "🌟", true),
    SECRETARY("Secretary", "सचिव / कार्यवाह", "✍️", true),
    TREASURER("Treasurer", "खजिनदार", "💰", true),
    COMMITTEE_MEMBER("Committee Member", "समिती सदस्य", "👥", false),
    VOLUNTEER("Volunteer", "स्वयंसेवक / कार्यकर्ता", "🤝", false),
    ADVISOR("Advisor", "वरिष्ठ सल्लागार", "📢", true)
}

enum class MemberDepartment(
    val title: String,
    val marathiTitle: String,
    val iconEmoji: String
) {
    CORE("Core Committee", "मुख्य पदाधिकारी", "🏛️"),
    POOJA_RITUAL("Pooja & Rituals", "पूजा व धार्मिक", "🪔"),
    MAHAPRASAD("Mahaprasad & Food", "महाप्रसाद व्यवस्था", "🍛"),
    MANDAP_SOUND("Mandap & Sound", "मंडप व ध्वनी", "🎪"),
    SECURITY_CROWD("Security & Crowd", "सुरक्षा व रांग नियोजन", "🛡️"),
    FINANCE_ACCOUNTS("Finance & Accounts", "हिशोब व निधी", "💼"),
    YOUTH_WING("Youth Wing", "युवा मंच", "⚡")
}

data class Member(
    val id: String,
    val name: String,
    val role: MemberRole,
    val department: MemberDepartment,
    val phoneNumber: String,
    val allocatedDuty: String = "",
    val bloodGroup: String = "",
    val address: String = "",
    val joiningYear: String = "२०२०",
    val isActive: Boolean = true
) {
    val initials: String
        get() {
            val parts = name.trim().split(" ")
            return if (parts.size >= 2) {
                "${parts[0].take(1)}${parts[1].take(1)}"
            } else {
                name.take(2)
            }
        }
}

data class MemberStats(
    val totalMembers: Int,
    val officeBearers: Int,
    val volunteers: Int,
    val onDutyToday: Int
)
