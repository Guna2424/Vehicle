package com.guna.vehicle.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.guna.vehicle.data.local.dao.VehicleDao
import com.guna.vehicle.data.local.entity.VehicleEntity

@Database(entities = [VehicleEntity::class], version = 2, exportSchema = false)
abstract class VehicleDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao

    companion object {
        @Volatile
        private var Instance: VehicleDatabase? = null

        fun getDatabase(context: Context): VehicleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, VehicleDatabase::class.java, "vehicle_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
