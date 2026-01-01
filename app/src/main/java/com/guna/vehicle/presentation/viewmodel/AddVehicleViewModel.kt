package com.guna.vehicle.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.guna.vehicle.data.local.entity.VehicleEntity
import com.guna.vehicle.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AddVehicleUiState(
    val brand: String = "",
    val model: String = "",
    val fuelType: String = "",
    val vehicleNumber: String = "",
    val year: String = "",
    val ownerName: String = "",
    val isFormValid: Boolean = false
)


@HiltViewModel
class AddVehicleViewModel @Inject constructor(private val repository: VehicleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddVehicleUiState())
    val uiState: StateFlow<AddVehicleUiState> = _uiState.asStateFlow()

    val brands = listOf("Honda", "Tata", "Hero", "Bajaj", "Yamaha", "TVS", "Suzuki", "Royal Enfield")
    val fuelTypes = listOf("Petrol", "Diesel", "Electric", "CNG")
    val models = listOf("Activa 4G", "Activa 5G", "Activa 6G", "Activa 125", "Nexon XM", "Pulsar 150", "Jupiter")
    val years = (2000..2025).map { it.toString() }.reversed()

    fun updateBrand(brand: String) {
        _uiState.update { it.copy(brand = brand) }
        validateForm()
    }

    fun updateModel(model: String) {
        _uiState.update { it.copy(model = model) }
        validateForm()
    }

    fun updateFuelType(fuelType: String) {
        _uiState.update { it.copy(fuelType = fuelType) }
        validateForm()
    }

    fun updateVehicleNumber(number: String) {
        _uiState.update { it.copy(vehicleNumber = number) }
        validateForm()
    }

    fun updateYear(year: String) {
        _uiState.update { it.copy(year = year) }
        validateForm()
    }

    fun updateOwnerName(name: String) {
        _uiState.update { it.copy(ownerName = name) }
        validateForm()
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid = state.brand.isNotBlank() &&
                state.model.isNotBlank() &&
                state.fuelType.isNotBlank() &&
                state.vehicleNumber.isNotBlank() &&
                state.year.isNotBlank() &&
                state.ownerName.isNotBlank()
        _uiState.update { it.copy(isFormValid = isValid) }
    }

    fun saveVehicle(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val vehicle = VehicleEntity(
                model = state.model,
                brand = state.brand,
                number = state.vehicleNumber,
                fuel = state.fuelType,
                year = state.year,
                ownerName = state.ownerName,
                purchaseTimestamp = System.currentTimeMillis()
            )
            repository.insertVehicle(vehicle)
            onSuccess()
        }
    }
}
