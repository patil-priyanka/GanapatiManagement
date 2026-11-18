package com.shreeganesh.ganapatimanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shreeganesh.ganapatimanagement.data.local.entity.FestivalSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FestivalSettingsDao {
    @Query("SELECT * FROM festival_settings WHERE id = 'default_festival' LIMIT 1")
    fun getSettings(): Flow<FestivalSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: FestivalSettingsEntity)

    @Query("SELECT COUNT(*) FROM festival_settings")
    suspend fun getCount(): Int
}
