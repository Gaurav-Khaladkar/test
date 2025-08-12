package com.example.realestate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestate.data.model.Property
import com.example.realestate.data.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val internalList = MutableStateFlow<List<Property>>(emptyList())

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeProperties()
        observeSearch()
    }

    private fun observeProperties() {
        repository.properties.onEach { list ->
            internalList.value = list
        }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        combine(internalList, searchQuery.debounce(250).distinctUntilChanged()) { list, query ->
            val filtered = if (query.isBlank()) list else list.filter { p ->
                p.title.contains(query, ignoreCase = true) ||
                p.address.contains(query, ignoreCase = true) ||
                p.city.contains(query, ignoreCase = true) ||
                p.state.contains(query, ignoreCase = true)
            }
            HomeUiState(properties = filtered, query = query)
        }.onEach { state -> _uiState.value = state }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(newValue: String) {
        searchQuery.value = newValue
    }

    fun onToggleFavorite(propertyId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(propertyId)
        }
    }
}

data class HomeUiState(
    val properties: List<Property> = emptyList(),
    val query: String = ""
)