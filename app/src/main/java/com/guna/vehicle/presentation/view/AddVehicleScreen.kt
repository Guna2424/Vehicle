package com.guna.vehicle.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guna.vehicle.VehicleApplication
import com.guna.vehicle.presentation.viewmodel.AddVehicleViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    navController: NavController,
    viewModel: AddVehicleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var showBrandSheet by remember { mutableStateOf(false) }
    var showModelSheet by remember { mutableStateOf(false) }
    var showFuelSheet by remember { mutableStateOf(false) }
    var showYearSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Vehicle", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { 
                    viewModel.saveVehicle {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = uiState.isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    disabledContainerColor = Color(0xFFBBDEFB)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Vehicle", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("VEHICLE DETAILS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
            
            SelectionField(
                label = "Brand",
                value = uiState.brand.ifEmpty { "Select a brand" },
                isPlaceholder = uiState.brand.isEmpty(),
                onClick = { showBrandSheet = true }
            )

            SelectionField(
                label = "Model",
                value = uiState.model.ifEmpty { "Select a model" },
                isPlaceholder = uiState.model.isEmpty(),
                onClick = { showModelSheet = true }
            )

            SelectionField(
                label = "Fuel Type",
                value = uiState.fuelType.ifEmpty { "Select fuel type" },
                isPlaceholder = uiState.fuelType.isEmpty(),
                onClick = { showFuelSheet = true }
            )

            CustomTextField(
                label = "Vehicle Number",
                value = uiState.vehicleNumber,
                placeholder = "Enter vehicle number (e.g., MH 12 AB 1234)",
                onValueChange = { viewModel.updateVehicleNumber(it) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("OTHER DETAILS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)

            SelectionField(
                label = "Year of Purchase",
                value = uiState.year.ifEmpty { "Select year of purchase" },
                isPlaceholder = uiState.year.isEmpty(),
                onClick = { showYearSheet = true }
            )

            CustomTextField(
                label = "Owner Name",
                value = uiState.ownerName,
                placeholder = "Enter owner's full name",
                onValueChange = { viewModel.updateOwnerName(it) }
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showBrandSheet) {
        SelectionBottomSheet(
            title = "Select Vehicle Brand",
            options = viewModel.brands,
            selectedOption = uiState.brand,
            onDismiss = { showBrandSheet = false },
            onOptionSelected = { viewModel.updateBrand(it) }
        )
    }

    if (showModelSheet) {
        SelectionBottomSheet(
            title = "Select Vehicle Model",
            options = viewModel.models,
            selectedOption = uiState.model,
            onDismiss = { showModelSheet = false },
            onOptionSelected = { viewModel.updateModel(it) }
        )
    }

    if (showFuelSheet) {
        SelectionBottomSheet(
            title = "Select Fuel Type",
            options = viewModel.fuelTypes,
            selectedOption = uiState.fuelType,
            onDismiss = { showFuelSheet = false },
            onOptionSelected = { viewModel.updateFuelType(it) }
        )
    }

    if (showYearSheet) {
        SelectionBottomSheet(
            title = "Select Year",
            options = viewModel.years,
            selectedOption = uiState.year,
            onDismiss = { showYearSheet = false },
            onOptionSelected = { viewModel.updateYear(it) }
        )
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF2196F3)
            )
        )
    }
}

@Composable
fun SelectionField(
    label: String,
    value: String,
    isPlaceholder: Boolean,
    onClick: () -> Unit
) {
    Column {
        Text(label, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = if (isPlaceholder) Color.LightGray else Color.Black,
                    fontSize = 16.sp
                )
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBottomSheet(
    title: String,
    options: List<String>,
    selectedOption: String,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(options.size) { index ->
                    val option = options[index]
                    val isSelected = option == selectedOption
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(option, fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                onOptionSelected(option)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2196F3))
                        )
                    }
                    if (index < options.size - 1) {
                         HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 1.dp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
