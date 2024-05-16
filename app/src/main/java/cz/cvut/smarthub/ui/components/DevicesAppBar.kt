@file:OptIn(ExperimentalMaterial3Api::class)

package cz.cvut.smarthub.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.cvut.smarthub.R
import cz.cvut.smarthub.ui.theme.SmartHubTheme

@Composable
fun BasicAppBar (
    title: String,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    actions: List<Triple<() -> Unit, ImageVector, String>> = emptyList(),
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        },
        actions = {
            actions.forEach {
                IconButton(onClick = it.first ) {
                    Icon(
                        imageVector = it.second,
                        contentDescription = it.third
                    )
                }
            }
        }
    )
}

@Composable
fun FullScreenAppBar(
    title: String,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = stringResource(R.string.cancel_button))
            }
        },
        actions = {
            TextButton(onClick = onSave, modifier = Modifier.padding(end = 16.dp)) {
                Text(text = stringResource(id = R.string.save_button))
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PreviewFullScreenAppBar() {
    SmartHubTheme {
        Scaffold(
            topBar = { FullScreenAppBar(
                onCancel = { /*TODO*/ },
                onSave = { /*TODO*/ },
                title = stringResource(id = R.string.edit_device_title)
            ) }
        ) {
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PreviewBasicAppBar() {
    SmartHubTheme {
        Scaffold(
            topBar = {
                BasicAppBar(
                    title = stringResource(id = R.string.devices_tab_title),
                    canNavigateBack = true,
                    onNavigateBack = {},
                    actions = listOf(
                        Triple(
                            {},
                            Icons.Outlined.Edit,
                            stringResource(id = R.string.edit_button)
                        ),
                        Triple(
                            {},
                            Icons.Outlined.ExitToApp,
                            stringResource(id = R.string.logout)
                        )
                    )
                )
            }
        ) {
        }
    }
}