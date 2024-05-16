package cz.cvut.smarthub.ui.screens.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cz.cvut.smarthub.R
import cz.cvut.smarthub.SmartHubScreen
import cz.cvut.smarthub.database.DashboardItem
import cz.cvut.smarthub.ui.components.BasicAppBar
import cz.cvut.smarthub.ui.components.DevicesBottomBar
import cz.cvut.smarthub.ui.utils.AppViewModelProvider

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val dashboardUiState by dashboardViewModel.dashboardItems.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                BasicAppBar(
                    title = stringResource(id = R.string.dashboard_tab_title),
                    canNavigateBack = false,
                    onNavigateBack = {},
                )
            }
        },
        bottomBar = {
            DevicesBottomBar(navController)
        },
    ) { innerPadding ->
        when (dashboardUiState) {
            is DashboardUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            is DashboardUiState.Success -> {
                if ((dashboardUiState as DashboardUiState.Success).items.isEmpty()) {
                    Text(
                        text = "No dashboard items available",
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    DashboardContent(
                        items = (dashboardUiState as DashboardUiState.Success).items,
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            is DashboardUiState.Error -> {
                Text(
                    text = "Error: ${(dashboardUiState as DashboardUiState.Error).exception.message}",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun DashboardContent(items: List<DashboardItem>, navController: NavHostController, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(items) { item ->
            DashboardItemCard(item, navController)
        }
    }
}

@Composable
fun DashboardItemCard(item: DashboardItem, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                when (item.title) {
                    "Energy Usage" -> navController.navigate(SmartHubScreen.EnergyUsage.name)
                    "Security Status" -> navController.navigate(SmartHubScreen.SecurityStatus.name)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = item.title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


