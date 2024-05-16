package cz.cvut.smarthub.ui.screens.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cz.cvut.smarthub.R
import cz.cvut.smarthub.SmartHubScreen
import cz.cvut.smarthub.database.Scene
import cz.cvut.smarthub.ui.components.BasicAppBar
import cz.cvut.smarthub.ui.components.ShowErrorScreen
import cz.cvut.smarthub.ui.components.ShowLoadingScreen
import cz.cvut.smarthub.ui.utils.AppViewModelProvider
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun SceneDetailScreen(
    navController: NavHostController,
    sceneDetailViewModel: SceneDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
) {
    val uiState by sceneDetailViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BasicAppBar(
                title = stringResource(id = R.string.scene_detail_title),
                canNavigateBack = true,
                onNavigateBack = {
                    navController.popBackStack()
                },
                actions =
                if (uiState is SceneUiState.Success) {
                    listOf(
                        Triple(
                            { navController.navigate("${SmartHubScreen.EditSceneDetail.name}/${(uiState as SceneUiState.Success).scene.id}") },
                            Icons.Outlined.Edit,
                            "Edit scene"
                        )
                    )
                } else emptyList()
            )
        }
    ) { innerPaddings ->
        when (val state = uiState) {
            is SceneUiState.Loading -> {
                ShowLoadingScreen(innerPaddings = innerPaddings)
            }
            is SceneUiState.Success -> {
                SceneDetailSuccessContent(innerPaddings = innerPaddings, scene = state.scene)
            }
            is SceneUiState.Error -> {
                ShowErrorScreen(innerPaddings = innerPaddings, errorMessage = state.exception.message ?: "Unknown error")
            }
        }
    }
}

@Composable
fun SceneDetailSuccessContent(scene: Scene, innerPaddings: PaddingValues) {
    Column {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = scene.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Description: ${scene.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = scene.isActive,
                    onCheckedChange = null,
                    enabled = false
                )
            }
            Text(
                text = "Start Time: ${scene.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}