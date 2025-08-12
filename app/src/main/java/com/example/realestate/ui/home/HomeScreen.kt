package com.example.realestate.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.realestate.data.model.Property
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    state: HomeUiState,
    onToggleFavorite: (Long) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPropertyClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Find your next home") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.query,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search city, address, or title") }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.properties, key = { it.id }) { property ->
                    PropertyCard(
                        property = property,
                        onFavoriteClick = { onToggleFavorite(property.id) },
                        onClick = { onPropertyClick(property.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyCard(
    property: Property,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = property.imageUrls.firstOrNull(),
                contentDescription = property.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = property.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${formatPrice(property.priceUsd)} • ${property.bedrooms} bd • ${property.bathrooms} ba • ${property.areaSqFt} sqft",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${property.address}, ${property.city}, ${property.state}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onFavoriteClick) {
                    val icon = if (property.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    Icon(imageVector = icon, contentDescription = "Favorite")
                }
            }
        }
    }
}

private fun formatPrice(value: Long): String = NumberFormat.getCurrencyInstance(Locale.US).format(value)

private object Icons {
    val Default = androidx.compose.material.icons.Icons.Default
}