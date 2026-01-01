package com.guna.vehicle.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guna.vehicle.VehicleApplication
import com.guna.vehicle.data.local.entity.VehicleEntity
import com.guna.vehicle.presentation.viewmodel.HomeUiState
import com.guna.vehicle.presentation.viewmodel.HomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Calendar

@Composable
fun HomeScreen(
    navController: androidx.navigation.NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = HomeUiState())

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(com.guna.vehicle.navigation.Screen.AddVehicleScreen.route) },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Vehicle", fontWeight = FontWeight.SemiBold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    DashboardHeader(
                        totalVehicles = uiState.totalVehicles.toString(),
                        totalEv = uiState.evCount.toString()
                    )
                }
                item {
                    InventorySection(
                        vehicles = uiState.vehicles,
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(
    totalVehicles: String,
    totalEv: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF0D1B2A),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.LightGray,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.padding(8.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Hi, Guna \uD83D\uDC4B",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Welcome back!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(
                title = totalVehicles,
                subtitle = "Total Vehicles",
                icon = Icons.Default.DirectionsCar,
                backgroundColor = Color(0xFFFFF3E0),
                iconColor = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = totalEv,
                subtitle = "Total EV",
                icon = Icons.Default.ElectricCar,
                backgroundColor = Color(0xFFE0F2F1),
                iconColor = Color(0xFFFFEB3B),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SummaryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun InventorySection(
    vehicles: List<VehicleEntity>,
    viewModel: HomeViewModel,
    uiState: HomeUiState
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    if (showFilterSheet) {
        FilterBottomSheet(
            viewModel = viewModel,
            onDismiss = { showFilterSheet = false },
            onApply = { showFilterSheet = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vehicle Inventory List",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = { showFilterSheet = true },
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            colors = if (uiState.isFilterActive) {
                ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFE3F2FD), contentColor = Color(0xFF2196F3)) 
            } else {
                 ButtonDefaults.outlinedButtonColors()
            }
        ) {
            Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Filter${if (uiState.isFilterActive) " (Active)" else ""}", color = if (uiState.isFilterActive) Color(0xFF2196F3) else Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F6F8))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Model & Brand", modifier = Modifier.weight(1.2f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Vehicle Number", modifier = Modifier.weight(1.2f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Fuel Type", modifier = Modifier.weight(0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Year of Purchase", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }

                vehicles.forEach { vehicle ->
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    VehicleRow(vehicle)
                }
                
                if (vehicles.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No vehicles match your filter.", color = Color.Gray)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}


@Composable
fun VehicleRow(vehicle: VehicleEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(vehicle.model, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(vehicle.brand, fontSize = 12.sp, color = Color.Gray)
        }
        Text(
            text = vehicle.number, 
            modifier = Modifier.weight(1.2f), 
            fontSize = 14.sp, 
            color = Color(0xFF2196F3),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = vehicle.fuel, 
            modifier = Modifier.weight(0.8f), 
            fontSize = 14.sp, 
            color = Color.Black
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(vehicle.year, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(calculateDuration(vehicle.year), fontSize = 10.sp, color = Color.Gray)
        }
    }
}

fun calculateDuration(yearStr: String): String {
    return try {
        val year = yearStr.toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val diff = currentYear - year
        if (diff <= 0) "New" else "$diff years"
    } catch (e: Exception) {
        "-"
    }
}
