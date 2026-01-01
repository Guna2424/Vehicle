package com.guna.vehicle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val model: String,
    val brand: String,
    val number: String,
    val fuel: String,
    val year: String,
    val ownerName: String, // Added field
    val purchaseTimestamp: Long // For duration calculation
)
