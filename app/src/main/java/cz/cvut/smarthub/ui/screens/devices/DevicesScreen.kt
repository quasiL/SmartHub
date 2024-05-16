package cz.cvut.smarthub.ui.screens.devices

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.cvut.smarthub.R
import cz.cvut.smarthub.database.Device
import cz.cvut.smarthub.SmartHubScreen
import cz.cvut.smarthub.ui.components.BasicAppBar
import cz.cvut.smarthub.ui.components.DevicesBottomBar
import cz.cvut.smarthub.ui.components.ShowErrorScreen
import cz.cvut.smarthub.ui.components.ShowLoadingScreen
import cz.cvut.smarthub.ui.utils.AppViewModelProvider
import cz.cvut.smarthub.ui.theme.SmartHubTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DevicesScreen (
    navController: NavHostController,
    devicesViewModel: DevicesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val devicesUiState: DevicesUiState by devicesViewModel.devices.collectAsStateWithLifecycle()

    val tabTitles = listOf("About", "Additional")
    val pagerState = rememberPagerState { tabTitles.size }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column {
                BasicAppBar(
                    title = stringResource(id = R.string.devices_tab_title),
                    canNavigateBack = false,
                    onNavigateBack = {},
                    actions = listOf(
                        Triple(
                            { devicesViewModel.deleteAllDevices() },
                            Icons.Outlined.Delete,
                            "Delete all devices"
                        ),
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("${SmartHubScreen.EditDeviceDetail.name}/0") }) {
                Icon(Icons.Outlined.Add, contentDescription = "Add device")
            }
        },
        bottomBar = {
            DevicesBottomBar(navController)
        },
    ) { innerPadding ->
        DevicesScreenContent(innerPadding, pagerState, devicesUiState, navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DevicesScreenContent(
    innerPadding: PaddingValues,
    pagerState: PagerState,
    playgroundsUiState: DevicesUiState,
    navController: NavHostController
) {
    HorizontalPager(state = pagerState, modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()) { index ->
        when (index) {
            0 -> {
                TabScreenList(playgroundsUiState, navController)
            }
            1 -> {
                TabScreenMap(playgroundsUiState)
            }
        }
    }
}

@Composable
fun TabScreenList(playgroundsUiState: DevicesUiState, navController: NavHostController) {
    when (playgroundsUiState) {
        is DevicesUiState.Error -> {
            ShowErrorScreen(
                errorMessage = playgroundsUiState.exception.message ?: "Error",
            )
        }
        DevicesUiState.Loading -> {
            ShowLoadingScreen()
        }
        is DevicesUiState.Success -> {
            ShowTabListSuccessScreen(
                playgroundsUiState.itemList,
                navController,
            )
        }
    }
}

@Composable
fun ShowTabListSuccessScreen(itemList: List<Device>, navController: NavHostController) {
    if (itemList.isEmpty()){
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_device_found),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        DevicesList(itemList, navController)
    }
}

@Composable
fun DevicesList(
    itemList: List<Device>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        items(items = itemList, key = { it.id }){playground ->
            DeviceItem(playground, { navController.navigate("${SmartHubScreen.DeviceDetail.name}/${playground.id}")} , modifier)
        }
    }
}

@Composable
fun DeviceItem(item: Device, onClick : () -> Unit, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(dimensionResource(id = androidx.loader.R.dimen.compat_button_padding_horizontal_material))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = androidx.loader.R.dimen.compat_button_padding_horizontal_material))
    ) {
        Column {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.baseline_aod_24),
                contentScale = ContentScale.Fit,
                contentDescription = "Device image"
            )
        }
        Column {
            Text (
                text = item.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text (
                text = item.ipAddress,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun DeviceItemPreview() {
    DeviceItem(Device(1, "Device 1", "Device 1 description"), {})
}


@Composable
fun TabScreenMap(playgroundsUiState: DevicesUiState) {
    Image(
        imageVector =  Icons.Outlined.Place,
        contentDescription = null,
        modifier = Modifier.width(300.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DevicesPreview() {
    SmartHubTheme {
        DevicesScreen(navController = rememberNavController())
    }
}