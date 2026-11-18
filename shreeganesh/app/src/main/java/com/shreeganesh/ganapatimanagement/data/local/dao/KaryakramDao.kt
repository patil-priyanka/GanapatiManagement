package com.shreeganesh.ganapatimanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shreeganesh.ganapatimanagement.data.local.entity.KaryakramEntity
import com.shreeganesh.ganapatimanagement.model.EventStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface KaryakramDao {
    @Query("SELECT * FROM karyakram ORDER BY dayNumber ASC, time ASC")
    fun getAllKaryakram(): Flow<List<KaryakramEntity>>

    @Query("SELECT * FROM karyakram WHERE dayNumber = :dayNumber ORDER BY time ASC")
    fun getKaryakramByDay(dayNumber: Int): Flow<List<KaryakramEntity>>

    @Query("SELECT * FROM karyakram WHERE id = :id LIMIT 1")
    suspend fun getKaryakramById(id: String): KaryakramEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKaryakram(item: KaryakramEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKaryakramList(items: List<KaryakramEntity>)

    @Update
    suspend fun updateKaryakram(item: KaryakramEntity)

    @Query("UPDATE karyakram SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: EventStatus)

    @Delete
    suspend fun deleteKaryakram(item: KaryakramEntity)

    @Query("DELETE FROM karyakram WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM karyakram")
    suspend fun getCount(): Int
}
