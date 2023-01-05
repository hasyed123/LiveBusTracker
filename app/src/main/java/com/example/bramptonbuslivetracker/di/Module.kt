package com.example.bramptonbuslivetracker.di

import android.app.Application
import android.content.Context
import com.example.bramptonbuslivetracker.network.vehicleposition.VehiclePositionApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}