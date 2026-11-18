package com.shreeganesh.ganapatimanagement.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shreeganesh.ganapatimanagement.data.local.converters.Converters
import com.shreeganesh.ganapatimanagement.data.local.dao.AartiDao
import com.shreeganesh.ganapatimanagement.data.local.dao.AnnouncementDao
import com.shreeganesh.ganapatimanagement.data.local.dao.DonationDao
import com.shreeganesh.ganapatimanagement.data.local.dao.ExpenseDao
import com.shreeganesh.ganapatimanagement.data.local.dao.FestivalSettingsDao
import com.shreeganesh.ganapatimanagement.data.local.dao.KaryakramDao
import com.shreeganesh.ganapatimanagement.data.local.dao.MemberDao
import com.shreeganesh.ganapatimanagement.data.local.entity.AartiEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.AnnouncementEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.DonationEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.ExpenseEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.FestivalSettingsEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.KaryakramEntity
import com.shreeganesh.ganapatimanagement.data.local.entity.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DonationEntity::class,
        ExpenseEntity::class,
        MemberEntity::class,
        KaryakramEntity::class,
        AartiEntity::class,
        AnnouncementEntity::class,
        FestivalSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun donationDao(): DonationDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun memberDao(): MemberDao
    abstract fun karyakramDao(): KaryakramDao
    abstract fun aartiDao(): AartiDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun festivalSettingsDao(): FestivalSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ganapati_management.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default seed data on database creation
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let {
                                    DatabaseSeeder.seedDatabaseIfEmpty(it)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance

                // Also trigger check to ensure data exists if database file was already created
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseSeeder.seedDatabaseIfEmpty(instance)
                }

                instance
            }
        }
    }
}
