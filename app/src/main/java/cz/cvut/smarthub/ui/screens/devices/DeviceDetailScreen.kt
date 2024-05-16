package cz.cvut.smarthub.ui.screens.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cz.cvut.smarthub.R
import cz.cvut.smarthub.SmartHubScreen
import cz.cvut.smarthub.database.Device
import cz.cvut.smarthub.ui.components.BasicAppBar
import cz.cvut.smarthub.ui.components.ShowErrorScreen
import cz.cvut.smarthub.ui.components.ShowLoadingScreen
import cz.cvut.smarthub.ui.utils.AppViewModelProvider

@Composable
fun DeviceDetailScreen(
    navController: NavHostController,
    deviceDetailViewModel: DeviceDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
) {
    val uiState by deviceDetailViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            BasicAppBar(
                title = stringResource(id = R.string.device_detail_title),
                canNavigateBack = true,
                onNavigateBack = {
                    navController.popBackStack()
                },
                actions =
                if (uiState is DeviceUiState.Success){
                    listOf(
                        Triple(
                            { navController.navigate("${SmartHubScreen.EditDeviceDetail.name}/${(uiState as DeviceUiState.Success).device.id}") },
                            Icons.Outlined.Edit,
                            "Edit device"
                        )
                    )
                }
                else emptyList()
            )
        }
    ) {innerPaddings ->
        when (val state = uiState) {
            is DeviceUiState.Loading -> {
                ShowLoadingScreen(innerPaddings = innerPaddings)
            }
            is DeviceUiState.Success -> {
                DeviceDetailSuccessContent(innerPaddings = innerPaddings, device = state.device)
            }
            is DeviceUiState.Error -> {
                ShowErrorScreen(innerPaddings = innerPaddings, errorMessage = state.exception.message ?: "Unknown error")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeviceDetailSuccessContent(device: Device, innerPaddings: PaddingValues) {
    Column {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = device.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Type: ${device.type}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Description: ${device.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Location: ${device.location}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "IP Address: ${device.ipAddress}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}