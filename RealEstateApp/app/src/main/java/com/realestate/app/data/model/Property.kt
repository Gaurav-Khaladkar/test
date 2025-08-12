package com.realestate.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Property(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val priceType: PriceType = PriceType.SALE,
    val propertyType: PropertyType = PropertyType.APARTMENT,
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val area: Double = 0.0,
    val areaUnit: AreaUnit = AreaUnit.SQFT,
    val address: Address = Address(),
    val images: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val features: List<String> = emptyList(),
    val yearBuilt: Int? = null,
    val parkingSpaces: Int = 0,
    val furnished: Boolean = false,
    val petFriendly: Boolean = false,
    val availableFrom: Date? = null,
    val ownerId: String = "",
    val ownerName: String = "",
    val ownerPhone: String = "",
    val ownerEmail: String = "",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val status: PropertyStatus = PropertyStatus.AVAILABLE,
    val views: Int = 0,
    val favorites: Int = 0,
    val latitude: Double? = null,
    val longitude: Double? = null
) : Parcelable

@Parcelize
data class Address(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val country: String = "",
    val fullAddress: String = ""
) : Parcelable

enum class PriceType {
    SALE, RENT, LEASE
}

enum class PropertyType {
    APARTMENT, HOUSE, CONDO, TOWNHOUSE, VILLA, PENTHOUSE, STUDIO, LOFT, DUPLEX, TRIPLEX
}

enum class AreaUnit {
    SQFT, SQM, ACRES, HECTARES
}

enum class PropertyStatus {
    AVAILABLE, SOLD, RENTED, UNDER_CONTRACT, PENDING, OFF_MARKET
}