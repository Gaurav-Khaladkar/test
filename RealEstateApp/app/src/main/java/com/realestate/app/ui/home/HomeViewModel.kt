package com.realestate.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realestate.app.data.model.Property
import com.realestate.app.data.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    fun refreshData() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                // Combine featured and recent properties
                combine(
                    propertyRepository.getProperties(),
                    propertyRepository.getProperties()
                ) { allProperties, _ ->
                    val featuredProperties = allProperties.filter { it.favorites > 10 }.take(5)
                    val recentProperties = allProperties.sortedByDescending { it.createdAt }.take(10)
                    
                    HomeUiState.Success(
                        featuredProperties = featuredProperties,
                        recentProperties = recentProperties
                    )
                }.catch { exception ->
                    _uiState.value = HomeUiState.Error(
                        message = exception.message ?: "Unknown error occurred"
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (exception: Exception) {
                _uiState.value = HomeUiState.Error(
                    message = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val featuredProperties: List<Property>,
        val recentProperties: List<Property>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}