package com.example.realestate.data.repository

import com.example.realestate.data.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    val properties: Flow<List<Property>>
    suspend fun toggleFavorite(propertyId: Long)
    suspend fun getProperty(propertyId: Long): Property?
    suspend fun search(query: String): List<Property>
}