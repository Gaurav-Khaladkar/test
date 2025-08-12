package com.realestate.app.data.repository

import com.realestate.app.data.model.User
import com.realestate.app.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun getUserById(id: String): Flow<User?>
    suspend fun updateUser(user: User)
    suspend fun updateUserPreferences(userId: String, preferences: UserPreferences)
    suspend fun addSavedSearch(userId: String, name: String, filters: UserPreferences)
    suspend fun removeSavedSearch(userId: String, searchId: String)
    suspend fun getSavedSearches(userId: String): Flow<List<String>>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun signOut()
}