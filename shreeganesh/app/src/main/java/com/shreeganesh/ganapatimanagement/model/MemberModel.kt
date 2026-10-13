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

fun getSampleMembers(): List<Member> {
    return listOf(
        Member(
            id = "mem_1",
            name = "विलासराव बाळकृष्ण पाटील",
            role = MemberRole.PRESIDENT,
            department = MemberDepartment.CORE,
            phoneNumber = "9822011223",
            allocatedDuty = "उत्सव मुख्य नियोजन व समन्वय",
            bloodGroup = "O+",
            address = "शिवाजी नगर, पुणे",
            joiningYear = "२००५"
        ),
        Member(
            id = "mem_2",
            name = "सचिन अनंत जोशी",
            role = MemberRole.VICE_PRESIDENT,
            department = MemberDepartment.MANDAP_SOUND,
            phoneNumber = "9890123456",
            allocatedDuty = "मंडप, स्टेज व सुरक्षा समन्वय",
            bloodGroup = "B+",
            address = "डेक्कन जिमखाना, पुणे",
            joiningYear = "२०१०"
        ),
        Member(
            id = "mem_3",
            name = "अमित प्रभाकर कुलकर्णी",
            role = MemberRole.TREASURER,
            department = MemberDepartment.FINANCE_ACCOUNTS,
            phoneNumber = "9860234567",
            allocatedDuty = "देणगी पावती व दैनिक खर्च हिशोब",
            bloodGroup = "A+",
            address = "शनिवार पेठ, पुणे",
            joiningYear = "२०१२"
        ),
        Member(
            id = "mem_4",
            name = "विकास गजानन शिंदे",
            role = MemberRole.SECRETARY,
            department = MemberDepartment.CORE,
            phoneNumber = "9765345678",
            allocatedDuty = "परवानग्या, पत्रव्यवहार व बैठक नियोजन",
            bloodGroup = "O+",
            address = "कोथरूड, पुणे",
            joiningYear = "२०१४"
        ),
        Member(
            id = "mem_5",
            name = "प्रशांत यशवंत मोरे",
            role = MemberRole.COMMITTEE_MEMBER,
            department = MemberDepartment.POOJA_RITUAL,
            phoneNumber = "9552456789",
            allocatedDuty = "सकाळ-संध्याकाळ महाआरती व पुरोहित व्यवस्था",
            bloodGroup = "AB+",
            address = "सदाशिव पेठ, पुणे",
            joiningYear = "२०१६"
        ),
        Member(
            id = "mem_6",
            name = "रोहित अशोक कदम",
            role = MemberRole.VOLUNTEER,
            department = MemberDepartment.MAHAPRASAD,
            phoneNumber = "9423567890",
            allocatedDuty = "दुपारचे महाप्रसाद अन्नदान रांग नियोजन",
            bloodGroup = "B+",
            address = "रविवार पेठ, पुणे",
            joiningYear = "२०२१"
        ),
        Member(
            id = "mem_7",
            name = "ओंकार सूर्यकांत पवार",
            role = MemberRole.VOLUNTEER,
            department = MemberDepartment.SECURITY_CROWD,
            phoneNumber = "9637678901",
            allocatedDuty = "दर्शन रांग व गर्दी नियंत्रण",
            bloodGroup = "O+",
            address = "कसबा पेठ, पुणे",
            joiningYear = "२०२२"
        ),
        Member(
            id = "mem_8",
            name = "सौ. स्मिता सुहास जोशी",
            role = MemberRole.COMMITTEE_MEMBER,
            department = MemberDepartment.YOUTH_WING,
            phoneNumber = "9823789012",
            allocatedDuty = "महिला भजन व सांस्कृतिक स्पर्धा व्यवस्था",
            bloodGroup = "A+",
            address = "शुक्रवार पेठ, पुणे",
            joiningYear = "२०१८"
        )
    )
}
