package com.example.realestate.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestate.data.model.Property
import com.example.realestate.data.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: PropertyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val propertyId: Long = savedStateHandle.get<Long>("propertyId") ?: -1L

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val p = repository.getProperty(propertyId)
            _uiState.value = DetailsUiState(property = p)
        }
    }

    fun onToggleFavorite() {
        val id = _uiState.value.property?.id ?: return
        viewModelScope.launch {
            repository.toggleFavorite(id)
            val updated = repository.getProperty(id)
            _uiState.value = _uiState.value.copy(property = updated)
        }
    }
}

data class DetailsUiState(
    val property: Property? = null
)