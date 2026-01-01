package com.guna.vehicle.repository

import com.guna.vehicle.data.local.dao.VehicleDao
import com.guna.vehicle.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val vehicleDao: VehicleDao) {
    val allVehicles: Flow<List<VehicleEntity>> = vehicleDao.getAllVehicles()
    
    val vehicleCount: Flow<Int> = vehicleDao.getVehicleCount()
    
    val evCount: Flow<Int> = vehicleDao.getEVCount()

    suspend fun insertVehicle(vehicle: VehicleEntity) {
        vehicleDao.insertVehicle(vehicle)
    }
}
