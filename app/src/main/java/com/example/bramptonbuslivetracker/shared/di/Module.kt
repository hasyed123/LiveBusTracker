package com.example.bramptonbuslivetracker.shared.di

import android.content.Context
import com.example.bramptonbuslivetracker.data.remote.VehiclePositionApi
import com.example.bramptonbuslivetracker.data.repository.VehiclePositionRepositoryImpl
import com.example.bramptonbuslivetracker.domain.repository.VehiclePositionRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun providesVehiclePositionApi(): VehiclePositionApi {
        return Retrofit.Builder()
            .baseUrl("https://nextride.brampton.ca:81")
            .addConverterFactory(
                GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            ))
            .build()
            .create(VehiclePositionApi::class.java)
    }

    @Provides
    @Singleton
    fun providesVehiclePositionRepository(api: VehiclePositionApi, @ApplicationContext context: Context): VehiclePositionRepository {
        return VehiclePositionRepositoryImpl(api, context)
    }

}