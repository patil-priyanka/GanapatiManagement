package com.shreeganesh.ganapatimanagement.data.local

import com.shreeganesh.ganapatimanagement.data.local.entity.AartiEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.AnnouncementEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.DonationEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.ExpenseEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.FestivalSettingsEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.KaryakramEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.MemberEntity
import com.shreeganesh.ganapatimanagement.model.DonationCategory
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.ExpenseCategory
import com.shreeganesh.ganapatimanagement.model.KaryakramCategory
import com.shreeganesh.ganapatimanagement.model.MemberDepartment
import com.shreeganesh.ganapatimanagement.model.MemberRole
import com.shreeganesh.ganapatimanagement.model.PaymentMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseSeeder {

    suspend fun seedDatabaseIfEmpty(database: AppDatabase) = withContext(Dispatchers.IO) {
        val donationDao = database.donationDao()
        val expenseDao = database.expenseDao()
        val memberDao = database.memberDao()
        val karyakramDao = database.karyakramDao()
        val aartiDao = database.aartiDao()
        val announcementDao = database.announcementDao()
        val settingsDao = database.festivalSettingsDao()

        /*if (donationDao.getCount() == 0) {
            donationDao.insertDonations(getInitialDonations())
        }

        if (expenseDao.getCount() == 0) {
            expenseDao.insertExpenses(getInitialExpenses())
        }

        if (memberDao.getCount() == 0) {
            memberDao.insertMembers(getInitialMembers())
        }

        if (karyakramDao.getCount() == 0) {
            karyakramDao.insertKaryakramList(getInitialKaryakram())
        }
*/
        if (aartiDao.getCount() == 0) {
            aartiDao.insertAartis(getInitialAartis())
        }

        /*if (announcementDao.getCount() == 0) {
            announcementDao.insertAnnouncement(
                AnnouncementEntity(
                    id = "ann_1",
                    title = "आज संध्याकाळी ७:३० वाजता महाआरती",
                    message = "स्थानिक आमदार व प्रतिष्ठित मान्यवरांच्या हस्ते महाआरती होईल. सर्व कार्यकर्त्यांनी उपस्थित राहावे.",
                    tag = "महत्त्वाचे",
                    timeAgo = "१ तास पूर्वी"
                )
            )
        }

        if (settingsDao.getCount() == 0) {
            settingsDao.insertOrUpdate(
                FestivalSettingsEntity(
                    id = "default_festival",
                    festivalTitle = "श्री गणेश उत्सव २०२६",
                    mandalName = "श्री सार्वजनिक गणेशोत्सव मंडळ",
                    location = "शिवाजी नगर • स्थापना १९९८",
                    motto = "॥ श्री गणेशाय नमः ॥",
                    currentDay = 3,
                    totalDays = 10,
                    targetBudget = 150000.0
                )
            )
        }*/
    }

   /* private fun getInitialDonations(): List<DonationEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            DonationEntity(
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
                note = "कुटुंबाच्या वतीने सदिच्छा देणगी",
                createdAtTimestamp = now - 600000
            ),
            DonationEntity(
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
                note = "महाप्रसाद अन्नदान प्रायोजकत्व",
                createdAtTimestamp = now - 3600000
            ),
            DonationEntity(
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
                note = "संध्या आरती नैवेद्य सेवा",
                createdAtTimestamp = now - 86400000
            ),
            DonationEntity(
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
                note = "मंडप विद्युत रोषणाई मदत",
                createdAtTimestamp = now - 90000000
            ),
            DonationEntity(
                id = "d5",
                receiptNumber = "१००",
                donorName = "अमित प्रकाश देशमुख",
                phoneNumber = "9765123980",
                amount = 1001.0,
                paymentMode = PaymentMode.CASH,
                category = DonationCategory.GENERAL,
                date = "०५ ऑक्टो २०२६",
                time = "०४:०० PM",
                address = "सदाशिव पेठ, पुणे",
                createdAtTimestamp = now - 95000000
            ),
            DonationEntity(
                id = "d6",
                receiptNumber = "०९९",
                donorName = "मयूर अशोक जाधव",
                phoneNumber = "9552123456",
                amount = 501.0,
                paymentMode = PaymentMode.UPI,
                category = DonationCategory.GENERAL,
                date = "०४ ऑक्टो २०२६",
                time = "११:१० AM",
                address = "पुणे",
                createdAtTimestamp = now - 172800000
            ),
            DonationEntity(
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
                note = "अन्नदान महाप्रसाद मुख्य प्रायोजक",
                createdAtTimestamp = now - 175000000
            )
        )
    }

    private fun getInitialExpenses(): List<ExpenseEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            ExpenseEntity(
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
                note = "मंडप स्टेज व मुख्य कमान उभारणी आगाऊ उचल",
                createdAtTimestamp = now - 1800000
            ),
            ExpenseEntity(
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
                note = "१० दिवसांचे जनरेटर भाडे व लाईट फिटिंग",
                createdAtTimestamp = now - 7200000
            ),
            ExpenseEntity(
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
                note = "आरती व भजन साऊंड व्यवस्था",
                createdAtTimestamp = now - 86400000
            ),
            ExpenseEntity(
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
                note = "५०० भाविकांसाठी महाप्रसाद भोजन साहित्य",
                createdAtTimestamp = now - 92000000
            ),
            ExpenseEntity(
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
                note = "सकाळ-संध्याकाळ पूजेसाठी फुले व हार",
                createdAtTimestamp = now - 98000000
            ),
            ExpenseEntity(
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
                note = "आगमन मिरवणूक मानधन आगाऊ धनादेश",
                createdAtTimestamp = now - 172800000
            )
        )
    }

    private fun getInitialMembers(): List<MemberEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            MemberEntity(
                id = "mem_1",
                name = "विलासराव बाळकृष्ण पाटील",
                role = MemberRole.PRESIDENT,
                department = MemberDepartment.CORE,
                phoneNumber = "9822011223",
                allocatedDuty = "उत्सव मुख्य नियोजन व समन्वय",
                bloodGroup = "O+",
                address = "शिवाजी नगर, पुणे",
                joiningYear = "२००५",
                createdAtTimestamp = now - 1000000
            ),
            MemberEntity(
                id = "mem_2",
                name = "सचिन अनंत जोशी",
                role = MemberRole.VICE_PRESIDENT,
                department = MemberDepartment.MANDAP_SOUND,
                phoneNumber = "9890123456",
                allocatedDuty = "मंडप, स्टेज व सुरक्षा समन्वय",
                bloodGroup = "B+",
                address = "डेक्कन जिमखाना, पुणे",
                joiningYear = "२०१०",
                createdAtTimestamp = now - 900000
            ),
            MemberEntity(
                id = "mem_3",
                name = "अमित प्रभाकर कुलकर्णी",
                role = MemberRole.TREASURER,
                department = MemberDepartment.FINANCE_ACCOUNTS,
                phoneNumber = "9860234567",
                allocatedDuty = "देणगी पावती व दैनिक खर्च हिशोब",
                bloodGroup = "A+",
                address = "शनिवार पेठ, पुणे",
                joiningYear = "२०१२",
                createdAtTimestamp = now - 800000
            ),
            MemberEntity(
                id = "mem_4",
                name = "विकास गजानन शिंदे",
                role = MemberRole.SECRETARY,
                department = MemberDepartment.CORE,
                phoneNumber = "9765345678",
                allocatedDuty = "परवानग्या, पत्रव्यवहार व बैठक नियोजन",
                bloodGroup = "O+",
                address = "कोथरूड, पुणे",
                joiningYear = "२०१४",
                createdAtTimestamp = now - 700000
            ),
            MemberEntity(
                id = "mem_5",
                name = "प्रशांत यशवंत मोरे",
                role = MemberRole.COMMITTEE_MEMBER,
                department = MemberDepartment.POOJA_RITUAL,
                phoneNumber = "9552456789",
                allocatedDuty = "सकाळ-संध्याकाळ महाआरती व पुरोहित व्यवस्था",
                bloodGroup = "AB+",
                address = "सदाशिव पेठ, पुणे",
                joiningYear = "२०१६",
                createdAtTimestamp = now - 600000
            ),
            MemberEntity(
                id = "mem_6",
                name = "रोहित अशोक कदम",
                role = MemberRole.VOLUNTEER,
                department = MemberDepartment.MAHAPRASAD,
                phoneNumber = "9423567890",
                allocatedDuty = "दुपारचे महाप्रसाद अन्नदान रांग नियोजन",
                bloodGroup = "B+",
                address = "रविवार पेठ, पुणे",
                joiningYear = "२०२१",
                createdAtTimestamp = now - 500000
            ),
            MemberEntity(
                id = "mem_7",
                name = "ओंकार सूर्यकांत पवार",
                role = MemberRole.VOLUNTEER,
                department = MemberDepartment.SECURITY_CROWD,
                phoneNumber = "9637678901",
                allocatedDuty = "दर्शन रांग व गर्दी नियंत्रण",
                bloodGroup = "O+",
                address = "कसबा पेठ, पुणे",
                joiningYear = "२०२२",
                createdAtTimestamp = now - 400000
            ),
            MemberEntity(
                id = "mem_8",
                name = "सौ. स्मिता सुहास जोशी",
                role = MemberRole.COMMITTEE_MEMBER,
                department = MemberDepartment.YOUTH_WING,
                phoneNumber = "9823789012",
                allocatedDuty = "महिला भजन व सांस्कृतिक स्पर्धा व्यवस्था",
                bloodGroup = "A+",
                address = "शुक्रवार पेठ, पुणे",
                joiningYear = "२०१८",
                createdAtTimestamp = now - 300000
            )
        )
    }

    private fun getInitialKaryakram(): List<KaryakramEntity> {
        return listOf(
            KaryakramEntity(
                id = "prog_1",
                title = "श्री गणेश मूर्ती आगमन मिरवणूक व प्रतिष्ठापना",
                category = KaryakramCategory.PROCESSION,
                dayNumber = 1,
                dateFormatted = "७ सप्टेंबर २०२६",
                time = "सकाळी ०८:००",
                endTime = "सकाळी ११:३०",
                location = "टिळक रस्ता ते मुख्य मंडप",
                inchargePerson = "विलासराव पाटील (अध्यक्ष)",
                inchargePhone = "9822011223",
                chiefGuest = "मा. स्थानिक आमदार व सर्व विश्वस्त",
                description = "पारंपरिक ढोल-ताशा गजर व मंगल वाद्यांच्या सुरात बाप्पाचे वाजत-गाजत मंडपात आगमन.",
                status = EventStatus.COMPLETED,
                highlights = listOf("ढोल-ताशा पथक", "गुलाल मुक्त मिरवणूक", "पारंपरिक वेशभूषा"),
                isPinned = true
            ),
            KaryakramEntity(
                id = "prog_2",
                title = "सकाळची महाआरती व मोदक नैवेद्य",
                category = KaryakramCategory.AARTI,
                dayNumber = 1,
                dateFormatted = "७ सप्टेंबर २०२६",
                time = "सकाळी ०८:००",
                endTime = "सकाळी ०८:४५",
                location = "मुख्य गणेश मंडप",
                inchargePerson = "प्रशांत मोरे (पूजा प्रमुख)",
                inchargePhone = "9552456789",
                chiefGuest = "सर्व भक्तगण व स्थानिक रहिवासी",
                description = "सुखकर्ता दुःखहर्ता व शेंदूर लाल चढायो महाआरती. २१ उकडीच्या मोदकांचा महानैवेद्य अर्पण.",
                status = EventStatus.COMPLETED,
                aartiId = "aarti_ganpati_1",
                highlights = listOf("२१ मोदक नैवेद्य", "पंचारती", "शंखनाद"),
                isPinned = false
            ),
            KaryakramEntity(
                id = "prog_3",
                title = "दुपारचे महाप्रसाद अन्नदान वाटप",
                category = KaryakramCategory.MAHAPRASAD,
                dayNumber = 1,
                dateFormatted = "७ सप्टेंबर २०२६",
                time = "दुपारी १२:३०",
                endTime = "दुपारी ०३:३०",
                location = "अन्नछत्र मंडप",
                inchargePerson = "रोहित कदम (अन्नदान समिती)",
                inchargePhone = "9423567890",
                chiefGuest = "जेष्ठ नागरिक संघ",
                description = "सर्व गणेशभक्तांसाठी मोफत महाप्रसाद वाटप (पुरी-भाजी, मसालेभात व शिरा).",
                status = EventStatus.COMPLETED,
                highlights = listOf("१,०००+ भाविक भोजन", "स्वच्छता व्यवस्था", "पाणी वाटप")
            ),
            KaryakramEntity(
                id = "prog_4",
                title = "सायंकाळची भव्य धूपारती व भजन",
                category = KaryakramCategory.AARTI,
                dayNumber = 2,
                dateFormatted = "८ सप्टेंबर २०२६",
                time = "संध्याकाळी ०७:३०",
                endTime = "रात्री ०८:३०",
                location = "मुख्य गणेश मंडप",
                inchargePerson = "सचिन जोशी (उपाध्यक्ष)",
                inchargePhone = "9890123456",
                chiefGuest = "श्री गजानन महाराज भजन मंडळ",
                description = "पारंपरिक वाद्यांच्या गजरात धूपारती व भक्तांचा सामुदायिक नामघोष.",
                status = EventStatus.COMPLETED,
                aartiId = "aarti_ganpati_2",
                highlights = listOf("धूपारती", "सामुदायिक जयघोष", "प्रसाद वितरण")
            ),
            KaryakramEntity(
                id = "prog_5",
                title = "शालेय विद्यार्थ्यांची चित्रकला व निबंध स्पर्धा",
                category = KaryakramCategory.COMPETITION,
                dayNumber = 3,
                dateFormatted = "९ सप्टेंबर २०२६",
                time = "सकाळी १०:००",
                endTime = "दुपारी ०१:००",
                location = "उत्सव सांस्कृतिक मंच",
                inchargePerson = "स्मिता जोशी (युवा मंच)",
                inchargePhone = "9823789012",
                chiefGuest = "स्थानिक शाळेचे मुख्याध्यापक",
                description = "विषय: 'पर्यावरणपूरक गणेशोत्सव व जलसंवर्धन'. सर्व सहभागी विद्यार्थ्यांना प्रमाणपत्रे.",
                status = EventStatus.COMPLETED,
                highlights = listOf("१००+ स्पर्धक", "आकर्षक पारितोषिके", "चित्र प्रदर्शन")
            ),
            KaryakramEntity(
                id = "prog_6",
                title = "संध्याकाळची महाआरती व मंत्रपुष्पांजली",
                category = KaryakramCategory.AARTI,
                dayNumber = 3,
                dateFormatted = "९ सप्टेंबर २०२६",
                time = "संध्याकाळी ०७:३०",
                endTime = "रात्री ०८:१५",
                location = "मुख्य गणेश मंडप",
                inchargePerson = "अमित कुलकर्णी (खजिनदार)",
                inchargePhone = "9860234567",
                chiefGuest = "मा. स्थानिक पोलीस निरीक्षक",
                description = "सद्गुरू कृपा महाआरती व मंत्रपुष्पांजली समर्पण.",
                status = EventStatus.LIVE_NOW,
                aartiId = "aarti_pushpanjali",
                highlights = listOf("थेट प्रक्षेपण", "मंत्रपुष्पांजली", "सुवर्ण पुष्प"),
                isPinned = true
            ),
            KaryakramEntity(
                id = "prog_7",
                title = "प्रसिद्ध कलाकारांची 'भक्ती संगीत संध्या'",
                category = KaryakramCategory.BHAJAN_KIRTAN,
                dayNumber = 3,
                dateFormatted = "९ सप्टेंबर २०२६",
                time = "रात्री ०८:३०",
                endTime = "रात्री ११:००",
                location = "उत्सव मुख्य रंगमंच",
                inchargePerson = "विकास शिंदे (सचिव)",
                inchargePhone = "9765345678",
                chiefGuest = "प्रसिद्ध शास्त्रीय गायक व मान्यवर",
                description = "नाट्यसंगीत, भावगीते व भक्तिगीतांचा सुरेल आविष्कार.",
                status = EventStatus.UPCOMING,
                highlights = listOf("शास्त्रीय गायन", "तबला जुगलबंदी", "लाईव्ह साऊंड"),
                isPinned = true
            ),
            KaryakramEntity(
                id = "prog_8",
                title = "गौरी आवाहन व मंगळागौर खेळ",
                category = KaryakramCategory.CULTURAL,
                dayNumber = 4,
                dateFormatted = "१० सप्टेंबर २०२६",
                time = "दुपारी ०४:००",
                endTime = "संध्याकाळी ०७:००",
                location = "उत्सव सभागृह",
                inchargePerson = "स्मिता जोशी",
                inchargePhone = "9823789012",
                chiefGuest = "महिला बचत गट अध्यक्षा",
                description = "पारंपरिक मंगळागौर खेळ, उखाणे स्पर्धा व हळदी-कुंकू समारंभ.",
                status = EventStatus.UPCOMING,
                highlights = listOf("पारंपरिक खेळ", "उखाणे स्पर्धा", "वाण वाटप")
            ),
            KaryakramEntity(
                id = "prog_9",
                title = "भव्य रक्तदान व मोफत आरोग्य तपासणी शिबीर",
                category = KaryakramCategory.SOCIAL_CAMP,
                dayNumber = 5,
                dateFormatted = "११ सप्टेंबर २०२६",
                time = "सकाळी ०९:००",
                endTime = "दुपारी ०२:००",
                location = "सेवा मंडप कक्ष",
                inchargePerson = "ओंकार पवार",
                inchargePhone = "9637678901",
                chiefGuest = "ससून हॉस्पिटल डॉक्टर्स टीम",
                description = "सामाजिक बांधिलकीतून भव्य रक्तदान शिबीर व मोफत मधुमेह-रक्तदाब तपासणी.",
                status = EventStatus.UPCOMING,
                highlights = listOf("रक्तदान कार्ड", "प्रशस्तिपत्र", "आरोग्य मार्गदर्शन"),
                isPinned = true
            ),
            KaryakramEntity(
                id = "prog_10",
                title = "अनंत चतुर्दशी विसर्जन मिरवणूक सोहळा",
                category = KaryakramCategory.PROCESSION,
                dayNumber = 10,
                dateFormatted = "१६ सप्टेंबर २०२६",
                time = "दुपारी ०२:००",
                endTime = "रात्री १०:००",
                location = "मुख्य चौक ते विसर्जन घाट",
                inchargePerson = "सर्व विश्वस्त मंडळ",
                inchargePhone = "9822011223",
                chiefGuest = "सर्व पुणेकर गणेशभक्त",
                description = "'गणपती बाप्पा मोरया, पुढच्या वर्षी लवकर या' च्या जयघोषात मंगलमय विसर्जन.",
                status = EventStatus.UPCOMING,
                highlights = listOf("पारंपरिक रथ", "गुलाल मुक्त", "आरती विसर्जन घाटावर"),
                isPinned = true
            )
        )
    }
*/
    private fun getInitialAartis(): List<AartiEntity> {
        return listOf(
            AartiEntity(
                id = "aarti_ganpati_1",
                title = "Sukhkarta Dukhharta",
                marathiTitle = "सुखकर्ता दुःखहर्ता",
                deity = "श्री गणेश",
                durationSeconds = 225,
                author = "समर्थ रामदास स्वामी",
                description = "श्री गणपतीची मुख्य आणि अत्यंत लोकप्रिय महाआरती",
                sortOrder = 1,
                lyrics = """
॥ श्री गणपतीची आरती ॥

सुखकर्ता दुःखहर्ता वार्ता विघ्नाची ।
नुरवी पूर्वी प्रेम कृपा जयाची ।
सर्वांगी सुंदर उटी शेंदुराची ।
कंठी झळके माळ मुक्ताफळांची ॥ १ ॥

जय देव जय देव जय मंगलमूर्ती ।
दर्शनमात्रे मनकामना पूर्ती ॥ धृ ॥

रत्नखचित फरा तुज गौरीकुमरा ।
चंदनाची उटी कुंकुमकेशरा ।
हीरेजडित मुकुट शोभतो बरा ।
रुणझुणती नूपुरे चरणी घागरिया ॥ २ ॥

जय देव जय देव जय मंगलमूर्ती ।
दर्शनमात्रे मनकामना पूर्ती ॥ धृ ॥

लंबोदर पीतांबर फणिवरबंधना ।
सरळ सोंड वक्रतुंड त्रिनयना ।
दास रामाचा वाट पाहे सदना ।
संकटी पावावे निर्वाणी रक्षावे सुरवरवंदना ॥ ३ ॥

जय देव जय देव जय मंगलमूर्ती ।
दर्शनमात्रे मनकामना पूर्ती ॥ धृ ॥
                """.trimIndent()
            ),
            AartiEntity(
                id = "aarti_ganpati_2",
                title = "Shendur Lal Chadhayo",
                marathiTitle = "शेंदूर लाल चढायो",
                deity = "श्री गणेश",
                durationSeconds = 195,
                author = "संत कवी",
                description = "सिंदूर शोभित गजमुख गणपतीची मधुर आरती",
                sortOrder = 2,
                lyrics = """
॥ शेंदूर लाल चढायो आरती ॥

शेंदूर लाल चढायो चांगो हरकुंवर का ।
महिषासुरमर्दिनी सुरवर वंदित चरणी का ॥
गौरीपुत्र विनायक मंगल दाता तू ।
ऋद्धि-सिद्धि के दाता जय जय मंगल तू ॥ १ ॥

जय जय श्री गणराज विद्यासुखदाता ।
धन्य तुम्हारो दर्शन सुरवर मुनि त्राता ॥ धृ ॥

अष्टसिद्धि नवनिधि के दाता तू स्वामी ।
भक्तन के प्रतिपालक अंतर के जामी ॥
मोदक भोग लगावे लडुआ को थाल ।
चरण शरण में आयो राखो दयाल ॥ २ ॥

जय जय श्री गणराज विद्यासुखदाता ।
धन्य तुम्हारो दर्शन सुरवर मुनि त्राता ॥ धृ ॥
                """.trimIndent()
            ),
            AartiEntity(
                id = "aarti_shankar",
                title = "Lavthavati Vikrala",
                marathiTitle = "लवथवती विक्राळा",
                deity = "श्री शंकर",
                durationSeconds = 210,
                author = "संत रामदास",
                description = "भगवान शंकराची रुद्र व कल्याणकारी आरती",
                sortOrder = 3,
                lyrics = """
॥ श्री शंकराची आरती ॥

लवथवती विक्राळा ब्रह्मांडी माळा ।
विषे कंठ काळा त्रिनेत्री ज्वाळा ।
लावण्यसुंदर मस्तकी बाळा ।
तेथुनिया जल निर्मळ वाहे झुळझुळा ॥ १ ॥

जय देव जय देव जय श्रीशंकरा ।
आरती ओवाळू तुज कर्पूरगौरा ॥ धृ ॥

कर्पूरगौर भोळा नयनी विशाळा ।
अर्धांगी पार्वती सुमनमाळा ।
विभूतीचे उधळण शितिकंठ नीळा ।
व्याघ्रांबर फणिधर सुंदर गळा ॥ २ ॥

जय देव जय देव जय श्रीशंकरा ।
आरती ओवाळू तुज कर्पूरगौरा ॥ धृ ॥
                """.trimIndent()
            ),
            AartiEntity(
                id = "aarti_durga",
                title = "Durge Durgat Bhari",
                marathiTitle = "दुर्गे दुर्घट भारी",
                deity = "श्री देवी",
                durationSeconds = 240,
                author = "संत रामदास",
                description = "आदिशक्ती तुळजाभवानी व दुर्गा मातेची महाआरती",
                sortOrder = 4,
                lyrics = """
॥ श्री देवीची आरती ॥

दुर्गे दुर्घट भारी तुजविण संसारी ।
अनाथनाथे अंबे करुणा विस्तारी ।
वारी वारी जन्ममरणाते वारी ।
हारी पडलो आता संकट निवारी ॥ १ ॥

जय देवी जय देवी जय महिषासुरमर्दिनी ।
सुरवरईश्वरवरदे तारक संजीवनी ॥ धृ ॥

त्रिभुवनी भुवनी पाहता तुज ऐसी नाही ।
चारी श्रमले परंतु न बोलवे काही ।
साही विवाद करिता पडिले प्रवाही ।
ते तू भक्तालागी पावसि लवलाही ॥ २ ॥

जय देवी जय देवी जय महिषासुरमर्दिनी ।
सुरवरईश्वरवरदे तारक संजीवनी ॥ धृ ॥
                """.trimIndent()
            ),
            AartiEntity(
                id = "aarti_ghalin_lotangan",
                title = "Ghalin Lotangan",
                marathiTitle = "घालीन लोटांगण",
                deity = "सर्व देव",
                durationSeconds = 180,
                author = "पारंपरिक",
                description = "सर्व पूजेनंतरची भावपूर्ण क्षमायाचना व समर्पण श्लोक",
                sortOrder = 5,
                lyrics = """
॥ घालीन लोटांगण प्रार्थना ॥

घालीन लोटांगण वंदीन चरण ।
डोळ्यांनी पाहीन रूप तुझे ।
प्रेमे आलिंगीन आनंदे पूजीन ।
भावे ओवाळीन म्हणे नामा ॥ १ ॥

त्वमेव माता च पिता त्वमेव ।
त्वमेव बंधुश्च सखा त्वमेव ।
त्वमेव विद्या द्रविणं त्वमेव ।
त्वमेव सर्वं मम देवदेव ॥ २ ॥

कायेन वाचा मनसेंद्रियैर्वा ।
बुद्ध्यात्मना वा प्रकृतिस्वभावात् ।
करोमि यद्यत् सकलं परस्मै ।
नारायणायेति समर्पयामि ॥ ३ ॥

अच्युतं केशवं रामनारायणं ।
कृष्णदामोदरं वासुदेवं हरिम् ।
श्रीधरं माधवं गोपिकावल्लभं ।
जानकीनायकं रामचंद्रं भजे ॥ ४ ॥
                """.trimIndent()
            ),
            AartiEntity(
                id = "aarti_pushpanjali",
                title = "Mantra Pushpanjali",
                marathiTitle = "मंत्र पुष्पांजली",
                deity = "वैदिक प्रार्थना",
                durationSeconds = 150,
                author = "ऋग्वेद - वैदिक ऋचा",
                description = "आरती समाप्तीनंतर अखंड राष्ट्रकल्याण वैदिक मंत्रपुष्पांजली",
                sortOrder = 6,
                lyrics = """
॥ मंत्र पुष्पांजली ॥

ॐ यज्ञेन यज्ञमयजन्त देवास्तानि धर्माणि प्रथमान्यासन् ।
ते ह नाकं महिमान: सचंत यत्र पूर्वे साध्या: संति देवा: ॥

ॐ राजाधिराजाय प्रसह्यसाहिने ।
नमो वयं वैश्रवणाय कुर्महे ।
स मे कामान् कामकामाय मह्यम् ।
कामेश्वरो वैश्रवणो ददातु ।
कुबेराय वैश्रवणाय महाराजाय नम: ॥

ॐ स्वस्ति । साम्राज्यं भौज्यं स्वाराज्यं
वैराज्यं पारमेष्ठ्यं राज्यं माहाराज्यमाधिपत्यमयं
समंतपर्यायी स्यात् सार्वभौम: सार्वायुष आंतादापरार्धात्
पृथिव्यै समुद्रपर्यंताया एकराळिति ॥

तदप्येष श्लोकोऽभिगीतो ।
मरुत्: परिवेष्टारो मरुत्तस्यावसन् गृहे ।
आविक्षितस्य कामप्रेर्विश्वेदेवा: सभासद इति ॥

॥ श्री मंगलमूर्ती मोरया ॥
॥ गणपती बाप्पा मोरया, पुढच्या वर्षी लवकर या ॥
                """.trimIndent()
            )
        )
    }
}
