package com.example.seoulpublicservice.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ReservationEntity::class], exportSchema = false, version = 1)
abstract class ReservationDatabase:RoomDatabase() {
    abstract fun getReservation() :ReservationDAO

    companion object {
        private var INSTANCE: ReservationDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
            }
        }

        fun getDatabase(context: Context): ReservationDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, ReservationDatabase::class.java, "reservation_table")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE as ReservationDatabase
        }
    }
}