package com.guna.vehicle.di

import android.content.Context
import androidx.room.Room
import com.guna.vehicle.data.local.VehicleDatabase
import com.guna.vehicle.data.local.dao.VehicleDao
import com.guna.vehicle.repository.VehicleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideVehicleDatabase(@ApplicationContext context: Context): VehicleDatabase {
        return VehicleDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideVehicleDao(database: VehicleDatabase): VehicleDao {
        return database.vehicleDao()
    }

    @Provides
    @Singleton
    fun provideVehicleRepository(dao: VehicleDao): VehicleRepository {
        return VehicleRepository(dao)
    }
}
