package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.KaryakramCategory
import com.shreeganesh.ganapatimanagement.model.KaryakramItem

@Entity(tableName = "karyakram")
data class KaryakramEntity(
    @PrimaryKey
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
    val isPinned: Boolean = false,
    val createdAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): KaryakramItem {
        return KaryakramItem(
            id = id,
            title = title,
            category = category,
            dayNumber = dayNumber,
            dateFormatted = dateFormatted,
            time = time,
            endTime = endTime,
            location = location,
            inchargePerson = inchargePerson,
            inchargePhone = inchargePhone,
            chiefGuest = chiefGuest,
            description = description,
            status = status,
            aartiId = aartiId,
            highlights = highlights,
            isPinned = isPinned
        )
    }

    companion object {
        fun fromDomainModel(item: KaryakramItem, timestamp: Long = System.currentTimeMillis()): KaryakramEntity {
            return KaryakramEntity(
                id = item.id,
                title = item.title,
                category = item.category,
                dayNumber = item.dayNumber,
                dateFormatted = item.dateFormatted,
                time = item.time,
                endTime = item.endTime,
                location = item.location,
                inchargePerson = item.inchargePerson,
                inchargePhone = item.inchargePhone,
                chiefGuest = item.chiefGuest,
                description = item.description,
                status = item.status,
                aartiId = item.aartiId,
                highlights = item.highlights,
                isPinned = item.isPinned,
                createdAtTimestamp = timestamp
            )
        }
    }
}
