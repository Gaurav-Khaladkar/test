package com.example.realestate.data.repository

import com.example.realestate.data.model.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FakePropertyRepository : PropertyRepository {
    private val state = MutableStateFlow(sampleProperties())
    private val mutex = Mutex()

    override val properties = state.asStateFlow()

    override suspend fun toggleFavorite(propertyId: Long) {
        mutex.withLock {
            val updated = state.value.map { p ->
                if (p.id == propertyId) p.copy(isFavorite = !p.isFavorite) else p
            }
            state.value = updated
        }
    }

    override suspend fun getProperty(propertyId: Long): Property? = state.value.firstOrNull { it.id == propertyId }

    override suspend fun search(query: String): List<Property> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return state.value
        return state.value.filter { p ->
            p.title.lowercase().contains(q) ||
            p.address.lowercase().contains(q) ||
            p.city.lowercase().contains(q) ||
            p.state.lowercase().contains(q)
        }
    }
}

private fun sampleProperties(): List<Property> = listOf(
    Property(
        id = 1L,
        title = "Modern Family Home",
        priceUsd = 650_000,
        address = "123 Maple Street",
        city = "Austin",
        state = "TX",
        zipCode = "73301",
        bedrooms = 4,
        bathrooms = 3,
        areaSqFt = 2400,
        lotSizeSqFt = 7400,
        latitude = 30.2672,
        longitude = -97.7431,
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1560185007-5f0bbf3b8c3a",
            "https://images.unsplash.com/photo-1570129477492-45c003edd2be",
            "https://images.unsplash.com/photo-1501183638710-841dd1904471"
        ),
        description = "A bright, open-concept home with a large backyard and upgraded kitchen.",
        isFavorite = false
    ),
    Property(
        id = 2L,
        title = "Downtown Loft",
        priceUsd = 420_000,
        address = "45 Pine Ave #302",
        city = "Denver",
        state = "CO",
        zipCode = "80014",
        bedrooms = 2,
        bathrooms = 2,
        areaSqFt = 1200,
        lotSizeSqFt = null,
        latitude = 39.7392,
        longitude = -104.9903,
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1493809842364-78817add7ffb",
            "https://images.unsplash.com/photo-1502005229762-cf1b2da7c52f"
        ),
        description = "Stylish loft with skyline views, high ceilings, and a private balcony.",
        isFavorite = true
    ),
    Property(
        id = 3L,
        title = "Cozy Suburban Ranch",
        priceUsd = 355_000,
        address = "789 Oak Drive",
        city = "Raleigh",
        state = "NC",
        zipCode = "27513",
        bedrooms = 3,
        bathrooms = 2,
        areaSqFt = 1500,
        lotSizeSqFt = 9800,
        latitude = 35.7796,
        longitude = -78.6382,
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1564013799919-ab600027ffc6",
            "https://images.unsplash.com/photo-1574365692937-c1e0cfe2a6d0"
        ),
        description = "Charming ranch with updated baths, a sunroom, and mature trees.",
        isFavorite = false
    )
)