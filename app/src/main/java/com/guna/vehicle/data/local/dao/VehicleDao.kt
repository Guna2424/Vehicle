package com.guna.vehicle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guna.vehicle.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles ORDER BY id DESC")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Query("SELECT COUNT(*) FROM vehicles")
    fun getVehicleCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM vehicles WHERE fuel = 'Electric'")
    fun getEVCount(): Flow<Int>
}
