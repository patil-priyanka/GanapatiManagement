package com.shreeganesh.ganapatimanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shreeganesh.ganapatimanagement.data.local.entity.AartiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AartiDao {
    @Query("SELECT * FROM aartis ORDER BY sortOrder ASC")
    fun getAllAartis(): Flow<List<AartiEntity>>

    @Query("SELECT * FROM aartis WHERE id = :id LIMIT 1")
    suspend fun getAartiById(id: String): AartiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAarti(aarti: AartiEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAartis(aartis: List<AartiEntity>)

    @Query("SELECT COUNT(*) FROM aartis")
    suspend fun getCount(): Int
}
