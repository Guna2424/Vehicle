package com.guna.vehicle.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guna.vehicle.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.fillMaxHeight(0.85f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Filter", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFF9F9F9))
                ) {
                    FilterSidebarItem(
                        text = "Brand",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        count = uiState.selectedBrands.size
                    )
                    FilterSidebarItem(
                        text = "Fuel Type",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        count = uiState.selectedFuelTypes.size
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    LazyColumn {
                        if (selectedTab == 0) {
                            items(viewModel.brands.size) { index ->
                                val brand = viewModel.brands[index]
                                val isChecked = uiState.selectedBrands.contains(brand)
                                FilterOptionItem(
                                    text = brand,
                                    isChecked = isChecked,
                                    onCheckedChange = { isSelected ->
                                        viewModel.updateBrandFilter(brand, isSelected)
                                    }
                                )
                            }
                        } else {
                            items(viewModel.fuelTypes.size) { index ->
                                val fuel = viewModel.fuelTypes[index]
                                val isChecked = uiState.selectedFuelTypes.contains(fuel)
                                FilterOptionItem(
                                    text = fuel,
                                    isChecked = isChecked,
                                    onCheckedChange = { isSelected ->
                                        viewModel.updateFuelFilter(fuel, isSelected)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.clearFilters() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDDDDD))
                ) {
                    Text("Clear all", color = Color.Gray)
                }
                
                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("Apply", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FilterSidebarItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    count: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) Color.White else Color.Transparent)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 20.dp)
                        .background(Color(0xFF2196F3), shape = RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            Text(
                text = text,
                color = if (isSelected) Color(0xFF2196F3) else Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
            
            if (count > 0) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun FilterOptionItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, fontSize = 14.sp, color = Color.Black)
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF2196F3),
                uncheckedColor = Color(0xFFE0E0E0)
            )
        )
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF5F5F5))
}
