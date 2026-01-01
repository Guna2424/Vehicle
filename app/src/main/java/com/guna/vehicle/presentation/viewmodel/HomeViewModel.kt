package com.guna.vehicle.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.guna.vehicle.data.local.entity.VehicleEntity
import com.guna.vehicle.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

data class HomeUiState(
    val vehicles: List<VehicleEntity> = emptyList(),
    val totalVehicles: Int = 0,
    val evCount: Int = 0,
    val selectedBrands: Set<String> = emptySet(),
    val selectedFuelTypes: Set<String> = emptySet(),
    val isFilterActive: Boolean = false
)


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: VehicleRepository) : ViewModel() {

    private val _filterState = MutableStateFlow(HomeUiState())

    val brands = listOf("Honda", "Tata", "Hero", "Bajaj", "Yamaha", "TVS", "Suzuki", "Royal Enfield", "Other")
    val fuelTypes = listOf("Petrol", "Diesel", "Electric", "CNG")

    val uiState: StateFlow<HomeUiState> = combine(
        repository.allVehicles,
        repository.vehicleCount,
        repository.evCount,
        _filterState
    ) { vehicles, total, ev, filterState ->
        
        var filteredVehicles = vehicles
        
        if (filterState.selectedBrands.isNotEmpty()) {
            filteredVehicles = filteredVehicles.filter { it.brand in filterState.selectedBrands }
        }
        
        if (filterState.selectedFuelTypes.isNotEmpty()) {
            filteredVehicles = filteredVehicles.filter { it.fuel in filterState.selectedFuelTypes }
        }

        HomeUiState(
            vehicles = filteredVehicles,
            totalVehicles = total,
            evCount = ev,
            selectedBrands = filterState.selectedBrands,
            selectedFuelTypes = filterState.selectedFuelTypes,
            isFilterActive = filterState.selectedBrands.isNotEmpty() || filterState.selectedFuelTypes.isNotEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun updateBrandFilter(brand: String, isSelected: Boolean) {
        val currentBrands = _filterState.value.selectedBrands.toMutableSet()
        if (isSelected) {
            currentBrands.add(brand)
        } else {
            currentBrands.remove(brand)
        }
        _filterState.update { it.copy(selectedBrands = currentBrands) }
    }

    fun updateFuelFilter(fuel: String, isSelected: Boolean) {
        val currentFuels = _filterState.value.selectedFuelTypes.toMutableSet()
        if (isSelected) {
            currentFuels.add(fuel)
        } else {
            currentFuels.remove(fuel)
        }
        _filterState.update { it.copy(selectedFuelTypes = currentFuels) }
    }

    fun clearFilters() {
        _filterState.update { 
            it.copy(
                selectedBrands = emptySet(),
                selectedFuelTypes = emptySet()
            ) 
        }
    }
}
