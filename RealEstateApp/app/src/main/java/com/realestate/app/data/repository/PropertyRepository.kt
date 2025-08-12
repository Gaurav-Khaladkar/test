package com.realestate.app.data.repository

import com.realestate.app.data.model.Property
import com.realestate.app.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun getProperties(): Flow<List<Property>>
    suspend fun getPropertyById(id: String): Flow<Property?>
    suspend fun searchProperties(filters: UserPreferences): Flow<List<Property>>
    suspend fun getPropertiesByLocation(latitude: Double, longitude: Double, radius: Double): Flow<List<Property>>
    suspend fun getFavoriteProperties(userId: String): Flow<List<Property>>
    suspend fun addToFavorites(userId: String, propertyId: String)
    suspend fun removeFromFavorites(userId: String, propertyId: String)
    suspend fun addProperty(property: Property): String
    suspend fun updateProperty(property: Property)
    suspend fun deleteProperty(propertyId: String)
    suspend fun getPropertiesByOwner(ownerId: String): Flow<List<Property>>
    suspend fun incrementViews(propertyId: String)
}