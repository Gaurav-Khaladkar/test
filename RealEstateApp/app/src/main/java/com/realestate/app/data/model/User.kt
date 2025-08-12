package com.realestate.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val profileImage: String = "",
    val userType: UserType = UserType.BUYER,
    val isVerified: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val preferences: UserPreferences = UserPreferences(),
    val favorites: List<String> = emptyList(),
    val savedSearches: List<SavedSearch> = emptyList()
) : Parcelable

@Parcelize
data class UserPreferences(
    val minPrice: Double = 0.0,
    val maxPrice: Double = Double.MAX_VALUE,
    val propertyTypes: List<PropertyType> = emptyList(),
    val minBedrooms: Int = 0,
    val maxBedrooms: Int = 10,
    val minBathrooms: Int = 0,
    val maxBathrooms: Int = 10,
    val minArea: Double = 0.0,
    val maxArea: Double = Double.MAX_VALUE,
    val locations: List<String> = emptyList(),
    val priceType: PriceType? = null,
    val petFriendly: Boolean? = null,
    val furnished: Boolean? = null
) : Parcelable

@Parcelize
data class SavedSearch(
    val id: String = "",
    val name: String = "",
    val filters: UserPreferences = UserPreferences(),
    val createdAt: Date = Date()
) : Parcelable

enum class UserType {
    BUYER, SELLER, AGENT, ADMIN
}