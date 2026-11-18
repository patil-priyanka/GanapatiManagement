package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.Donation
import com.shreeganesh.ganapatimanagement.model.DonationCategory
import com.shreeganesh.ganapatimanagement.model.PaymentMode

@Entity(tableName = "donations")
data class DonationEntity(
    @PrimaryKey
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
    val note: String = "",
    val createdAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): Donation {
        return Donation(
            id = id,
            receiptNumber = receiptNumber,
            donorName = donorName,
            phoneNumber = phoneNumber,
            amount = amount,
            paymentMode = paymentMode,
            category = category,
            date = date,
            time = time,
            address = address,
            note = note
        )
    }

    companion object {
        fun fromDomainModel(donation: Donation, timestamp: Long = System.currentTimeMillis()): DonationEntity {
            return DonationEntity(
                id = donation.id,
                receiptNumber = donation.receiptNumber,
                donorName = donation.donorName,
                phoneNumber = donation.phoneNumber,
                amount = donation.amount,
                paymentMode = donation.paymentMode,
                category = donation.category,
                date = donation.date,
                time = donation.time,
                address = donation.address,
                note = donation.note,
                createdAtTimestamp = timestamp
            )
        }
    }
}
