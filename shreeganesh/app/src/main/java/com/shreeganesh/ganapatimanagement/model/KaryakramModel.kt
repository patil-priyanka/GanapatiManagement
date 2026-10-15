package com.shreeganesh.ganapatimanagement.model

enum class KaryakramCategory(
    val title: String,
    val marathiTitle: String,
    val iconEmoji: String,
    val colorHex: Long
) {
    ALL("All", "सर्व", "🚩", 0xFFE65100),
    AARTI("Aarti & Pooja", "आरती व पूजा", "🪔", 0xFFE65100),
    MAHAPRASAD("Mahaprasad", "महाप्रसाद", "🍛", 0xFFD84315),
    CULTURAL("Cultural", "सांस्कृतिक व संगीत", "🎭", 0xFF8E24AA),
    BHAJAN_KIRTAN("Bhajan & Kirtan", "भजन व कीर्तन", "🎶", 0xFF1976D2),
    COMPETITION("Competitions", "विविध स्पर्धा", "🏆", 0xFFF57C00),
    SOCIAL_CAMP("Social Camps", "सामाजिक उपक्रम", "🩸", 0xFFC2185B),
    PROCESSION("Procession", "मिरवणूक व ढोल-ताशा", "🎺", 0xFF00897B),
    OTHER("Other", "इतर कार्यक्रम", "✨", 0xFF546E7A)
}

enum class EventStatus(
    val title: String,
    val marathiTitle: String,
    val badgeEmoji: String
) {
    UPCOMING("Upcoming", "आगामी", "🔔"),
    LIVE_NOW("Live Now", "सुरू आहे", "🔴"),
    COMPLETED("Completed", "संपन्न", "✓")
}

data class FestivalDayInfo(
    val dayNumber: Int,
    val dayTitleMarathi: String,
    val dateText: String,
    val tithi: String,
    val isMainDay: Boolean = false
)

data class KaryakramItem(
    val id: String,
    val title: String,
    val category: KaryakramCategory,
    val dayNumber: Int,
    val dateFormatted: String,
    val time: String,
    val endTime: String = "",
    val location: String,
    val inchargePerson: String,
    val inchargePhone: String = "",
    val chiefGuest: String = "",
    val description: String = "",
    val status: EventStatus = EventStatus.UPCOMING,
    val aartiId: String? = null,
    val highlights: List<String> = emptyList(),
    val isPinned: Boolean = false
)

data class KaryakramStats(
    val totalCount: Int,
    val liveCount: Int,
    val upcomingCount: Int,
    val completedCount: Int
)

fun getFestivalDaysList(): List<FestivalDayInfo> {
    return listOf(
        FestivalDayInfo(1, "दिवस १ • स्थापना", "७ सप्टें", "भाद्रपद शुक्ल चतुर्थी", true),
        FestivalDayInfo(2, "दिवस २ • ऋषिपंचमी", "८ सप्टें", "भाद्रपद शुक्ल पंचमी"),
        FestivalDayInfo(3, "दिवस ३ • बालसंस्कार", "९ सप्टें", "भाद्रपद शुक्ल षष्ठी"),
        FestivalDayInfo(4, "दिवस ४ • गौरी आवाहन", "१० सप्टें", "भाद्रपद शुक्ल सप्तमी", true),
        FestivalDayInfo(5, "दिवस ५ • गौरी पूजन व महाप्रसाद", "११ सप्टें", "भाद्रपद शुक्ल अष्टमी", true),
        FestivalDayInfo(6, "दिवस ६ • गौरी विसर्जन", "१२ सप्टें", "भाद्रपद शुक्ल नवमी"),
        FestivalDayInfo(7, "दिवस ७ • संगीत संध्या", "१३ सप्टें", "भाद्रपद शुक्ल दशमी"),
        FestivalDayInfo(8, "दिवस ८ • रक्तदान शिबीर", "१४ सप्टें", "भाद्रपद शुक्ल एकादशी"),
        FestivalDayInfo(9, "दिवस ९ • दीपोत्सव", "१५ सप्टें", "भाद्रपद शुक्ल द्वादशी"),
        FestivalDayInfo(10, "दिवस १० • विसर्जन मिरवणूक", "१६ सप्टें", "अनंत चतुर्दशी", true)
    )
}
