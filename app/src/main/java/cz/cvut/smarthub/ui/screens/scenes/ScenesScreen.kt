package cz.cvut.smarthub.ui.screens.scenes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import kotlinx.coroutines.launch
import cz.cvut.smarthub.database.Scene
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScenesScreen(
    navController: NavHostController,
    scenesViewModel: ScenesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val scenesUiState: ScenesUiState by scenesViewModel.scenes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                BasicAppBar(
                    title = stringResource(id = R.string.scenes_tab_title),
                    canNavigateBack = false,
                    onNavigateBack = {},
                    actions = listOf(
                        Triple(
                            { scenesViewModel.deleteAllScenes() },
                            Icons.Outlined.Delete,
                            "Delete all scenes"
                        ),
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("${SmartHubScreen.EditSceneDetail.name}/0") }) {
                Icon(Icons.Outlined.Add, contentDescription = "Add scene")
            }
        },
        bottomBar = {
            DevicesBottomBar(navController)
        },
    ) { innerPadding ->
        ScenesScreenContent(innerPadding, scenesUiState, navController)
    }
}

@Composable
fun ScenesScreenContent(
    innerPadding: PaddingValues,
    scenesUiState: ScenesUiState,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        when (scenesUiState) {
            is ScenesUiState.Error -> {
                item {
                    ShowErrorScreen(
                        errorMessage = scenesUiState.exception.message ?: "Error",
                    )
                }
            }
            ScenesUiState.Loading -> {
                item {
                    ShowLoadingScreen()
                }
            }
            is ScenesUiState.Success -> {
                if (scenesUiState.itemList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_scene_found),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(items = scenesUiState.itemList, key = { it.id }) { scene ->
                        SceneItem(scene, { navController.navigate("${SmartHubScreen.SceneDetail.name}/${scene.id}") })
                    }
                }
            }
        }
    }
}

@Composable
fun ShowScenesSuccessScreen(itemList: List<Scene>, navController: NavHostController) {
    if (itemList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_scene_found),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        ScenesList(itemList, navController)
    }
}

@Composable
fun ScenesList(
    itemList: List<Scene>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        items(items = itemList, key = { it.id }) { scene ->
            SceneItem(scene, { navController.navigate("${SmartHubScreen.SceneDetail.name}/${scene.id}") }, modifier)
        }
    }
}

@Composable
fun SceneItem(item: Scene, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate to scene detail",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun SceneItemPreview() {
    SceneItem(Scene(1, "Scene 1", "Scene 1 description"), {})
}

@Preview(showBackground = true)
@Composable
fun ScenesPreview() {
    SmartHubTheme {
        ScenesScreen(navController = rememberNavController())
    }
}