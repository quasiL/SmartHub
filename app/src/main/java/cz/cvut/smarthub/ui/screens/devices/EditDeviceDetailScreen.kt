package cz.cvut.smarthub.ui.screens.devices

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.CaptureManager
import cz.cvut.smarthub.R
import cz.cvut.smarthub.database.Device
import cz.cvut.smarthub.ui.components.ShowErrorScreen
import cz.cvut.smarthub.ui.components.ShowLoadingScreen
import cz.cvut.smarthub.ui.utils.AppViewModelProvider
import com.journeyapps.barcodescanner.CompoundBarcodeView

@Composable
fun EditDeviceDetailScreen(
    navController: NavHostController,
    deviceDetailViewModel: DeviceDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
) {
    val uiState by deviceDetailViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is DeviceUiState.Error -> {
            ShowErrorScreen(
                errorMessage = state.exception.message ?: "Error",
                onClick = { navController.popBackStack() }
            )
        }
        DeviceUiState.Loading -> {
            ShowLoadingScreen()
        }
        is DeviceUiState.Success -> {
            ShowSuccessScreen(
                state.device,
                navController,
                deviceDetailViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSuccessScreen(
    device: Device,
    navController: NavHostController,
    deviceViewModel: DeviceDetailViewModel
) {
    val nameState = rememberSaveable { mutableStateOf(device.name) }
    val typeState = rememberSaveable { mutableStateOf(device.type) }
    val descriptionState = rememberSaveable { mutableStateOf(device.description) }
    val locationState = rememberSaveable { mutableStateOf(device.location) }
    val ipAddressState = rememberSaveable { mutableStateOf(device.ipAddress) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.devices_tab_title)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(R.string.cancel_button)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            deviceViewModel.saveDevice(
                                newName = nameState.value,
                                newType = typeState.value,
                                newDescription = descriptionState.value,
                                newLocation = locationState.value,
                                newIpAddress = ipAddressState.value
                            )
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.save_button))
                    }
                }
            )
        },
    ) { innerPadding ->
        EditDeviceDetailSuccessContent(
            innerPadding,
            nameState,
            typeState,
            descriptionState,
            locationState,
            ipAddressState
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditDeviceDetailSuccessContent(
    innerPadding: PaddingValues,
    nameState: MutableState<String>,
    typeState: MutableState<String>,
    descriptionState: MutableState<String>,
    locationState: MutableState<String>,
    ipAddressState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(false) }
    val showQrCodeScanner = remember { mutableStateOf(false) }

    val context = LocalContext.current
    var scanFlag by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (showQrCodeScanner.value) {
            QrCodeScanner(
                onQrCodeScanned = { scannedData ->
                    nameState.value = "Device Name"
                    typeState.value = "Device Type"
                    descriptionState.value = "Device Description"
                    locationState.value = "Device Location"
                    ipAddressState.value = "192.168.0.1"
                    showQrCodeScanner.value = false
                },
                onCancel = {
                    showQrCodeScanner.value = false
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val spaceModifier = modifier
                    .fillMaxWidth()
                    .padding(24.dp, 4.dp)
                Image(
                    painter = painterResource(id = R.drawable.outline_add_a_photo_24),
                    contentDescription = "Scan QR code",
                    contentScale = ContentScale.Inside,
                    modifier = modifier
                        .size(200.dp, 200.dp)
                        .border(BorderStroke(1.dp, Color.Black))
                        .clickable {
                            showQrCodeScanner.value = true
                        }
                )
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = { Text(stringResource(id = R.string.name_placeholder)) },
                    modifier = spaceModifier
                )
                OutlinedTextField(
                    value = typeState.value,
                    onValueChange = { typeState.value = it },
                    label = { Text(stringResource(id = R.string.type_placeholder)) },
                    modifier = spaceModifier
                )
                OutlinedTextField(
                    value = descriptionState.value,
                    onValueChange = { descriptionState.value = it },
                    label = { Text(stringResource(id = R.string.description_placeholder)) },
                    modifier = spaceModifier
                )
                OutlinedTextField(
                    value = locationState.value,
                    onValueChange = { locationState.value = it },
                    label = { Text(stringResource(id = R.string.location_placeholder)) },
                    modifier = spaceModifier
                )
                OutlinedTextField(
                    value = ipAddressState.value,
                    onValueChange = { ipAddressState.value = it },
                    label = { Text(stringResource(id = R.string.ip_address_placeholder)) },
                    modifier = spaceModifier
                )
            }
        }
    }
}

@Composable
fun QrCodeScanner(
    onQrCodeScanned: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var qrCodeScanned by remember { mutableStateOf(false) }

    AndroidView(
        factory = { ctx ->
            val compoundBarcodeView = CompoundBarcodeView(ctx)
            val capture = CaptureManager(ctx as Activity, compoundBarcodeView)
            capture.initializeFromIntent(ctx.intent, null)
            compoundBarcodeView.setStatusText("")
            compoundBarcodeView.decodeContinuous { result ->
                if (!qrCodeScanned) {
                    qrCodeScanned = true
                    onQrCodeScanned(result.text)
                }
            }
            compoundBarcodeView
        },
        modifier = modifier,
        update = { view ->
            when (lifecycleOwner.lifecycle.currentState) {
                Lifecycle.State.RESUMED -> view.resume()
                Lifecycle.State.STARTED -> view.pause()
                else -> Unit
            }
            if (qrCodeScanned) {
                view.post { onCancel() }
            }
        }
    )
}
