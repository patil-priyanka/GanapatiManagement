package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.FestivalSummary

@Entity(tableName = "festival_settings")
data class FestivalSettingsEntity(
    @PrimaryKey
    val id: String = "default_festival",
    val festivalTitle: String = "श्री गणेश उत्सव २०२६",
    val mandalName: String = "श्री सार्वजनिक गणेशोत्सव मंडळ",
    val location: String = "शिवाजी नगर • स्थापना १९९८",
    val motto: String = "॥ श्री गणेशाय नमः ॥",
    val currentDay: Int = 3,
    val totalDays: Int = 10,
    val targetBudget: Double = 150000.0
) {
    fun toFestivalSummary(
        totalDonation: Double,
        donorCount: Int,
        totalExpense: Double,
        volunteerCount: Int
    ): FestivalSummary {
        return FestivalSummary(
            festivalTitle = festivalTitle,
            mandalName = mandalName,
            location = location,
            motto = motto,
            currentDay = currentDay,
            totalDays = totalDays,
            totalDonation = totalDonation,
            totalExpense = totalExpense,
            targetBudget = targetBudget,
            donorCount = donorCount,
            volunteerCount = volunteerCount
        )
    }
}
