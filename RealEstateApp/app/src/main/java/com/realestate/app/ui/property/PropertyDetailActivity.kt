package com.realestate.app.ui.property

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.realestate.app.R
import com.realestate.app.data.model.Property
import com.realestate.app.databinding.ActivityPropertyDetailBinding
import com.realestate.app.ui.adapter.PropertyImageAdapter
import com.realestate.app.ui.adapter.PropertyAmenitiesAdapter
import com.realestate.app.ui.adapter.PropertyFeaturesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PropertyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding
    private val viewModel: PropertyDetailViewModel by viewModels()
    
    private lateinit var imageAdapter: PropertyImageAdapter
    private lateinit var amenitiesAdapter: PropertyAmenitiesAdapter
    private lateinit var featuresAdapter: PropertyFeaturesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        setupObservers()
        setupClickListeners()

        // Get property from intent
        val property = intent.getParcelableExtra<Property>("property")
        property?.let {
            viewModel.setProperty(it)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.property_details)
        }
    }

    private fun setupRecyclerViews() {
        // Property Images RecyclerView
        imageAdapter = PropertyImageAdapter()
        binding.rvPropertyImages.apply {
            layoutManager = LinearLayoutManager(this@PropertyDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
        
        // Add snap helper for better scrolling
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvPropertyImages)

        // Amenities RecyclerView
        amenitiesAdapter = PropertyAmenitiesAdapter()
        binding.rvAmenities.apply {
            layoutManager = LinearLayoutManager(this@PropertyDetailActivity)
            adapter = amenitiesAdapter
        }

        // Features RecyclerView
        featuresAdapter = PropertyFeaturesAdapter()
        binding.rvFeatures.apply {
            layoutManager = LinearLayoutManager(this@PropertyDetailActivity)
            adapter = featuresAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: PropertyDetailUiState) {
        when (state) {
            is PropertyDetailUiState.Loading -> {
                binding.progressBar.visibility = android.view.View.VISIBLE
                binding.contentGroup.visibility = android.view.View.GONE
            }
            is PropertyDetailUiState.Success -> {
                binding.progressBar.visibility = android.view.View.GONE
                binding.contentGroup.visibility = android.view.View.VISIBLE
                
                val property = state.property
                displayPropertyDetails(property)
            }
            is PropertyDetailUiState.Error -> {
                binding.progressBar.visibility = android.view.View.GONE
                binding.contentGroup.visibility = android.view.View.GONE
                // Show error message
            }
        }
    }

    private fun displayPropertyDetails(property: Property) {
        // Set property images
        imageAdapter.submitList(property.images)
        
        // Set basic property information
        binding.tvPropertyTitle.text = property.title
        binding.tvPropertyPrice.text = formatPrice(property.price, property.priceType)
        binding.tvPropertyDescription.text = property.description
        
        // Set property details
        binding.tvBedrooms.text = property.bedrooms.toString()
        binding.tvBathrooms.text = property.bathrooms.toString()
        binding.tvArea.text = "${property.area} ${property.areaUnit.name.lowercase()}"
        binding.tvPropertyType.text = property.propertyType.name.replace("_", " ").capitalize()
        
        // Set address
        binding.tvAddress.text = property.address.fullAddress
        
        // Set amenities and features
        amenitiesAdapter.submitList(property.amenities)
        featuresAdapter.submitList(property.features)
        
        // Set additional details
        property.yearBuilt?.let { binding.tvYearBuilt.text = it.toString() }
        binding.tvParkingSpaces.text = property.parkingSpaces.toString()
        binding.tvFurnished.text = if (property.furnished) getString(R.string.yes) else getString(R.string.no)
        binding.tvPetFriendly.text = if (property.petFriendly) getString(R.string.yes) else getString(R.string.no)
        
        // Set owner information
        binding.tvOwnerName.text = property.ownerName
        binding.tvOwnerPhone.text = property.ownerPhone
        binding.tvOwnerEmail.text = property.ownerEmail
        
        // Set status
        binding.tvStatus.text = property.status.name.replace("_", " ").capitalize()
        binding.tvStatus.setBackgroundResource(getStatusColor(property.status))
        
        // Set views and favorites
        binding.tvViews.text = property.views.toString()
        binding.tvFavorites.text = property.favorites.toString()
    }

    private fun setupClickListeners() {
        // Contact owner buttons
        binding.btnCallOwner.setOnClickListener {
            val property = viewModel.getCurrentProperty()
            property?.let {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${it.ownerPhone}")
                startActivity(intent)
            }
        }

        binding.btnEmailOwner.setOnClickListener {
            val property = viewModel.getCurrentProperty()
            property?.let {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:${it.ownerEmail}")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry about ${it.title}")
                startActivity(intent)
            }
        }

        binding.btnViewOnMap.setOnClickListener {
            val property = viewModel.getCurrentProperty()
            property?.let {
                if (it.latitude != null && it.longitude != null) {
                    val gmmIntentUri = Uri.parse("geo:${it.latitude},${it.longitude}?q=${it.address.fullAddress}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent)
                    }
                }
            }
        }

        binding.btnShareProperty.setOnClickListener {
            val property = viewModel.getCurrentProperty()
            property?.let {
                val shareText = "Check out this property: ${it.title} - ${formatPrice(it.price, it.priceType)}"
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(intent, getString(R.string.share_property)))
            }
        }

        binding.btnAddToFavorites.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun formatPrice(price: Double, priceType: com.realestate.app.data.model.PriceType): String {
        val formatter = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US)
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
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_property_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_favorite -> {
                viewModel.toggleFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}