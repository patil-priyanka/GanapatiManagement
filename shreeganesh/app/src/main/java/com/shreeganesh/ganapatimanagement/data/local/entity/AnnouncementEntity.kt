package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.AnnouncementItem

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val tag: String = "महत्त्वाचे",
    val timeAgo: String = "१ तास",
    val createdAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): AnnouncementItem {
        return AnnouncementItem(
            id = id,
            title = title,
            message = message,
            tag = tag,
            timeAgo = timeAgo
        )
    }

    companion object {
        fun fromDomainModel(item: AnnouncementItem, timestamp: Long = System.currentTimeMillis()): AnnouncementEntity {
            return AnnouncementEntity(
                id = item.id,
                title = item.title,
                message = item.message,
                tag = item.tag,
                timeAgo = item.timeAgo,
                createdAtTimestamp = timestamp
            )
        }
    }
}
