package com.shreeganesh.ganapatimanagement.data.repository

import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.local.entity.DonationEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.ExpenseEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.FestivalSettingsEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.KaryakramEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.MemberEntity
import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.AartiEvent
import com.shreeganesh.ganapatimanagement.model.AartiItem
import com.shreeganesh.ganapatimanagement.model.AnnouncementItem
import com.shreeganesh.ganapatimanagement.model.Donation
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.Expense
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import com.shreeganesh.ganapatimanagement.model.KaryakramItem
import com.shreeganesh.ganapatimanagement.model.Member
import com.shreeganesh.ganapatimanagement.model.RecentActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GanapatiRepositoryImpl(private val database: AppDatabase) : GanapatiRepository {

    private val donationDao = database.donationDao()
    private val expenseDao = database.expenseDao()
    private val memberDao = database.memberDao()
    private val karyakramDao = database.karyakramDao()
    private val aartiDao = database.aartiDao()
    private val announcementDao = database.announcementDao()
    private val settingsDao = database.festivalSettingsDao()

    override val allDonations: Flow<List<Donation>> = donationDao.getAllDonations().map { list ->
        list.map { it.toDomainModel() }
    }

    override val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses().map { list ->
        list.map { it.toDomainModel() }
    }

    override val allMembers: Flow<List<Member>> = memberDao.getAllMembers().map { list ->
        list.map { it.toDomainModel() }
    }

    override val allKaryakram: Flow<List<KaryakramItem>> = karyakramDao.getAllKaryakram().map { list ->
        list.map { it.toDomainModel() }
    }

    override val allAartis: Flow<List<AartiItem>> = aartiDao.getAllAartis().map { list ->
        list.map { it.toDomainModel() }
    }

    override val latestAnnouncement: Flow<AnnouncementItem?> =
        announcementDao.getLatestAnnouncement().map { it?.toDomainModel() }

    override val festivalSummary: Flow<FestivalSummary> = combine(
        donationDao.getTotalDonationAmount(),
        donationDao.getDonorCount(),
        expenseDao.getTotalExpenseAmount(),
        memberDao.getMemberCount(),
        settingsDao.getSettings()
    ) { totalDonation, donorCount, totalExpense, memberCount, settings ->
        val activeSettings = settings ?: FestivalSettingsEntity()
        activeSettings.toFestivalSummary(
            totalDonation = totalDonation ?: 0.0,
            donorCount = donorCount,
            totalExpense = totalExpense ?: 0.0,
            volunteerCount = memberCount
        )
    }

    override val recentActivities: Flow<List<RecentActivity>> = combine(
        donationDao.getRecentDonations(5),
        expenseDao.getRecentExpenses(5)
    ) { donations, expenses ->
        val donationActs = donations.map { d ->
            val timeDiffMinutes = ((System.currentTimeMillis() - d.createdAtTimestamp) / 60000).coerceAtLeast(1)
            val timeAgoText = when {
                timeDiffMinutes < 60 -> "$timeDiffMinutes मि. पूर्वी"
                timeDiffMinutes < 1440 -> "${timeDiffMinutes / 60} तास पूर्वी"
                else -> "${timeDiffMinutes / 1440} दिवस पूर्वी"
            }
            RecentActivity(
                id = "act_don_${d.id}",
                title = d.donorName,
                subtitle = "पावती क्र. #${d.receiptNumber} • ${d.paymentMode.marathiName}",
                amount = d.amount,
                isDonation = true,
                category = "देणगी",
                timeAgo = timeAgoText
            )
        }
        val expenseActs = expenses.map { e ->
            val timeDiffMinutes = ((System.currentTimeMillis() - e.createdAtTimestamp) / 60000).coerceAtLeast(1)
            val timeAgoText = when {
                timeDiffMinutes < 60 -> "$timeDiffMinutes मि. पूर्वी"
                timeDiffMinutes < 1440 -> "${timeDiffMinutes / 60} तास पूर्वी"
                else -> "${timeDiffMinutes / 1440} दिवस पूर्वी"
            }
            RecentActivity(
                id = "act_exp_${e.id}",
                title = e.title,
                subtitle = "${e.vendorName} • व्हाऊचर #${e.voucherNumber}",
                amount = e.amount,
                isDonation = false,
                category = "खर्च",
                timeAgo = timeAgoText
            )
        }
        (donationActs + expenseActs).take(6)
    }

    override val todaySchedule: Flow<List<AartiEvent>> = karyakramDao.getAllKaryakram().map { items ->
        val relevantItems = items.filter { it.dayNumber == 3 }.ifEmpty { items.take(4) }
        relevantItems.map { item ->
            AartiEvent(
                id = item.id,
                title = item.title,
                marathiTitle = item.title,
                time = item.time,
                location = item.location,
                isNext = item.status == EventStatus.LIVE_NOW,
                isCompleted = item.status == EventStatus.COMPLETED,
                prasadDetails = if (item.highlights.isNotEmpty())
                    item.highlights.joinToString(" • ")
                else
                    item.description.take(30),
                host = item.inchargePerson
            )
        }
    }

    override suspend fun addDonation(donation: Donation) {
        donationDao.insertDonation(DonationEntity.fromDomainModel(donation))
    }

    override suspend fun deleteDonation(donationId: String) {
        donationDao.deleteById(donationId)
    }

    override suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(ExpenseEntity.fromDomainModel(expense))
    }

    override suspend fun deleteExpense(expenseId: String) {
        expenseDao.deleteById(expenseId)
    }

    override suspend fun addMember(member: Member) {
        memberDao.insertMember(MemberEntity.fromDomainModel(member))
    }

    override suspend fun deleteMember(memberId: String) {
        memberDao.deleteById(memberId)
    }

    override suspend fun addKaryakram(item: KaryakramItem) {
        karyakramDao.insertKaryakram(KaryakramEntity.fromDomainModel(item))
    }

    override suspend fun updateKaryakram(item: KaryakramItem) {
        karyakramDao.updateKaryakram(KaryakramEntity.fromDomainModel(item))
    }

    override suspend fun updateKaryakramStatus(id: String, status: EventStatus) {
        karyakramDao.updateStatus(id, status)
    }

    override suspend fun deleteKaryakram(id: String) {
        karyakramDao.deleteById(id)
    }
}
