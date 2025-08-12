package com.realestate.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.realestate.app.R
import com.realestate.app.databinding.FragmentHomeBinding
import com.realestate.app.ui.adapter.FeaturedPropertiesAdapter
import com.realestate.app.ui.adapter.RecentPropertiesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var featuredPropertiesAdapter: FeaturedPropertiesAdapter
    private lateinit var recentPropertiesAdapter: RecentPropertiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
        setupClickListeners()
    }

    private fun setupUI() {
        setupRecyclerViews()
        setupSwipeRefresh()
    }

    private fun setupRecyclerViews() {
        // Featured Properties RecyclerView
        featuredPropertiesAdapter = FeaturedPropertiesAdapter { property ->
            val action = HomeFragmentDirections.actionHomeToPropertyDetail(property)
            findNavController().navigate(action)
        }
        
        binding.rvFeaturedProperties.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredPropertiesAdapter
        }

        // Recent Properties RecyclerView
        recentPropertiesAdapter = RecentPropertiesAdapter { property ->
            val action = HomeFragmentDirections.actionHomeToPropertyDetail(property)
            findNavController().navigate(action)
        }
        
        binding.rvRecentProperties.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentPropertiesAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: HomeUiState) {
        when (state) {
            is HomeUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.contentGroup.visibility = View.GONE
                binding.errorView.visibility = View.GONE
            }
            is HomeUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.contentGroup.visibility = View.VISIBLE
                binding.errorView.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
                
                featuredPropertiesAdapter.submitList(state.featuredProperties)
                recentPropertiesAdapter.submitList(state.recentProperties)
                
                // Update property counts
                binding.tvFeaturedCount.text = getString(R.string.featured_properties_count, state.featuredProperties.size)
                binding.tvRecentCount.text = getString(R.string.recent_properties_count, state.recentProperties.size)
            }
            is HomeUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.contentGroup.visibility = View.GONE
                binding.errorView.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
                
                binding.tvErrorMessage.text = state.message
            }
        }
    }

    private fun setupClickListeners() {
        // Search bar click
        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }

        // Filter button click
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_filter)
        }

        // View all featured properties
        binding.btnViewAllFeatured.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_featured_properties)
        }

        // View all recent properties
        binding.btnViewAllRecent.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_recent_properties)
        }

        // Error retry button
        binding.btnRetry.setOnClickListener {
            viewModel.refreshData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}