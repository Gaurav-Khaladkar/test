package com.realestate.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.realestate.app.R
import com.realestate.app.data.model.Property
import com.realestate.app.databinding.ItemFeaturedPropertyBinding
import java.text.NumberFormat
import java.util.Locale

class FeaturedPropertiesAdapter(
    private val onPropertyClick: (Property) -> Unit
) : ListAdapter<Property, FeaturedPropertiesAdapter.FeaturedPropertyViewHolder>(PropertyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedPropertyViewHolder {
        val binding = ItemFeaturedPropertyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeaturedPropertyViewHolder(binding, onPropertyClick)
    }

    override fun onBindViewHolder(holder: FeaturedPropertyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeaturedPropertyViewHolder(
        private val binding: ItemFeaturedPropertyBinding,
        private val onPropertyClick: (Property) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.root.setOnClickListener { onPropertyClick(property) }

            // Load property image
            if (property.images.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(property.images.first())
                    .placeholder(R.drawable.placeholder_property)
                    .error(R.drawable.placeholder_property)
                    .centerCrop()
                    .into(binding.ivPropertyImage)
            }

            // Set property details
            binding.tvPropertyTitle.text = property.title
            binding.tvPropertyPrice.text = formatPrice(property.price, property.priceType)
            binding.tvPropertyLocation.text = property.address.city + ", " + property.address.state
            
            // Set property features
            val features = mutableListOf<String>()
            if (property.bedrooms > 0) features.add("${property.bedrooms} bed")
            if (property.bathrooms > 0) features.add("${property.bathrooms} bath")
            if (property.area > 0) features.add("${property.area} ${property.areaUnit.name.lowercase()}")
            
            binding.tvPropertyFeatures.text = features.joinToString(" • ")

            // Set status badge
            binding.tvStatus.text = property.status.name.replace("_", " ").capitalize()
            binding.tvStatus.setBackgroundResource(getStatusColor(property.status))

            // Set favorite count
            binding.tvFavoriteCount.text = property.favorites.toString()
        }

        private fun formatPrice(price: Double, priceType: com.realestate.app.data.model.PriceType): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            val formattedPrice = formatter.format(price)
            return when (priceType) {
                com.realestate.app.data.model.PriceType.SALE -> formattedPrice
                com.realestate.app.data.model.PriceType.RENT -> "$formattedPrice/month"
                com.realestate.app.data.model.PriceType.LEASE -> "$formattedPrice/lease"
            }
        }

        private fun getStatusColor(status: com.realestate.app.data.model.PropertyStatus): Int {
            return when (status) {
                com.realestate.app.data.model.PropertyStatus.AVAILABLE -> R.drawable.bg_status_available
                com.realestate.app.data.model.PropertyStatus.SOLD -> R.drawable.bg_status_sold
                com.realestate.app.data.model.PropertyStatus.RENTED -> R.drawable.bg_status_rented
                com.realestate.app.data.model.PropertyStatus.UNDER_CONTRACT -> R.drawable.bg_status_pending
                com.realestate.app.data.model.PropertyStatus.PENDING -> R.drawable.bg_status_pending
                com.realestate.app.data.model.PropertyStatus.OFF_MARKET -> R.drawable.bg_status_off_market
            }
        }

        private fun String.capitalize(): String {
            return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    private class PropertyDiffCallback : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }
}