package com.shreeganesh.ganapatimanagement.domain.repository

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

interface GanapatiRepository {
    val allDonations: Flow<List<Donation>>
    val allExpenses: Flow<List<Expense>>
    val allMembers: Flow<List<Member>>
    val allKaryakram: Flow<List<KaryakramItem>>
    val allAartis: Flow<List<AartiItem>>
    val latestAnnouncement: Flow<AnnouncementItem?>
    val festivalSummary: Flow<FestivalSummary>
    val recentActivities: Flow<List<RecentActivity>>
    val todaySchedule: Flow<List<AartiEvent>>

    suspend fun addDonation(donation: Donation)
    suspend fun deleteDonation(donationId: String)

    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpense(expenseId: String)

    suspend fun addMember(member: Member)
    suspend fun deleteMember(memberId: String)

    suspend fun addKaryakram(item: KaryakramItem)
    suspend fun updateKaryakram(item: KaryakramItem)
    suspend fun updateKaryakramStatus(id: String, status: EventStatus)
    suspend fun deleteKaryakram(id: String)
}
