package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.AartiItem

@Entity(tableName = "aartis")
data class AartiEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val marathiTitle: String,
    val deity: String,
    val durationSeconds: Int,
    val author: String,
    val lyrics: String,
    val description: String = "",
    val sortOrder: Int = 0
) {
    fun toDomainModel(): AartiItem {
        return AartiItem(
            id = id,
            title = title,
            marathiTitle = marathiTitle,
            deity = deity,
            durationSeconds = durationSeconds,
            author = author,
            lyrics = lyrics,
            description = description
        )
    }

    companion object {
        fun fromDomainModel(item: AartiItem, sortOrder: Int = 0): AartiEntity {
            return AartiEntity(
                id = item.id,
                title = item.title,
                marathiTitle = item.marathiTitle,
                deity = item.deity,
                durationSeconds = item.durationSeconds,
                author = item.author,
                lyrics = item.lyrics,
                description = item.description,
                sortOrder = sortOrder
            )
        }
    }
}
