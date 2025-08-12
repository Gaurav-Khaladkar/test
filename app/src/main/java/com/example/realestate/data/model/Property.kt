package com.example.realestate.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class Property(
    val id: Long,
    val title: String,
    val priceUsd: Long,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val areaSqFt: Int,
    val lotSizeSqFt: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val imageUrls: List<String>,
    val description: String,
    val isFavorite: Boolean = false
)