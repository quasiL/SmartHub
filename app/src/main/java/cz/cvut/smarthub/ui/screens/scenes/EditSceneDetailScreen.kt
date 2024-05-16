package cz.cvut.smarthub.ui.screens.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cz.cvut.smarthub.R
import cz.cvut.smarthub.database.Scene
import cz.cvut.smarthub.ui.components.ShowErrorScreen
import cz.cvut.smarthub.ui.components.ShowLoadingScreen
import cz.cvut.smarthub.ui.utils.AppViewModelProvider

@Composable
fun EditSceneDetailScreen(
    navController: NavHostController,
    sceneDetailViewModel: SceneDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
) {
    val uiState by sceneDetailViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is SceneUiState.Error -> {
            ShowErrorScreen(
                errorMessage = state.exception.message ?: "Error",
                onClick = { navController.popBackStack() }
            )
        }
        SceneUiState.Loading -> {
            ShowLoadingScreen()
        }
        is SceneUiState.Success -> {
            ShowSuccessScreen(
                state.scene,
                navController,
                sceneDetailViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSuccessScreen(
    scene: Scene,
    navController: NavHostController,
    sceneViewModel: SceneDetailViewModel
) {
    val nameState = rememberSaveable { mutableStateOf(scene.name) }
    val descriptionState = rememberSaveable { mutableStateOf(scene.description) }
    val isActiveState = rememberSaveable { mutableStateOf(scene.isActive) }
    val startTimeState = rememberSaveable { mutableStateOf(scene.startTime) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.scenes_tab_title)) },
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
                            sceneViewModel.saveScene(
                                newName = nameState.value,
                                newDescription = descriptionState.value,
                                newIsActive = isActiveState.value,
                                newStartTime = startTimeState.value
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
        EditSceneDetailSuccessContent(
            innerPadding,
            nameState,
            descriptionState,
            isActiveState
        )
    }
}

@Composable
fun EditSceneDetailSuccessContent(
    innerPadding: PaddingValues,
    nameState: MutableState<String>,
    descriptionState: MutableState<String>,
    isActiveState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        val spaceModifier = modifier
            .fillMaxWidth()
            .padding(24.dp, 4.dp)

        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text(stringResource(id = R.string.name_placeholder)) },
            modifier = spaceModifier
        )
        OutlinedTextField(
            value = descriptionState.value,
            onValueChange = { descriptionState.value = it },
            label = { Text(stringResource(id = R.string.description_placeholder)) },
            modifier = spaceModifier
        )
        Row(
            modifier = spaceModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.active_label),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isActiveState.value,
                onCheckedChange = { isActiveState.value = it }
            )
        }
        Row(
            modifier = spaceModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.start_time_label),
                modifier = Modifier.weight(1f)
            )
        }
    }
}