package com.example.realestate.di

import com.example.realestate.data.repository.FakePropertyRepository
import com.example.realestate.data.repository.PropertyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePropertyRepository(): PropertyRepository = FakePropertyRepository()
}