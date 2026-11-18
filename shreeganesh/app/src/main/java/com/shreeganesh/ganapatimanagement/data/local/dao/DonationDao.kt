package com.shreeganesh.ganapatimanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shreeganesh.ganapatimanagement.data.local.entity.DonationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DonationDao {
    @Query("SELECT * FROM donations ORDER BY createdAtTimestamp DESC")
    fun getAllDonations(): Flow<List<DonationEntity>>

    @Query("SELECT * FROM donations WHERE id = :id LIMIT 1")
    suspend fun getDonationById(id: String): DonationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DonationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonations(donations: List<DonationEntity>)

    @Update
    suspend fun updateDonation(donation: DonationEntity)

    @Delete
    suspend fun deleteDonation(donation: DonationEntity)

    @Query("DELETE FROM donations WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT SUM(amount) FROM donations")
    fun getTotalDonationAmount(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM donations")
    fun getDonorCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM donations")
    suspend fun getCount(): Int

    @Query("SELECT * FROM donations ORDER BY createdAtTimestamp DESC LIMIT :limit")
    fun getRecentDonations(limit: Int = 5): Flow<List<DonationEntity>>
}
