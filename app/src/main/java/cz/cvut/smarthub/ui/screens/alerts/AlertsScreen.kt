package cz.cvut.smarthub.ui.screens.alerts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cz.cvut.smarthub.R
import cz.cvut.smarthub.ui.components.BasicAppBar
import cz.cvut.smarthub.ui.components.DevicesBottomBar
import cz.cvut.smarthub.ui.utils.AppViewModelProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import cz.cvut.smarthub.AlertScheduler
import cz.cvut.smarthub.database.Alert
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlertsScreen (
    navController: NavHostController,
    alertViewModel: AlertViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val context = LocalContext.current
    val alerts by alertViewModel.alerts.collectAsState()

    LaunchedEffect(Unit) {
        AlertScheduler.scheduleAlerts(context, alertViewModel)
    }

    Scaffold(
        topBar = {
            Column {
                BasicAppBar(
                    title = stringResource(id = R.string.alerts_tab_title),
                    canNavigateBack = false,
                    onNavigateBack = {},
                )
            }
        },
        bottomBar = {
            DevicesBottomBar(navController)
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(top = 4.dp),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(alerts) { alert ->
                    AlertItem(alert,
                        onDelete = {
                            alertViewModel.deleteAlert(alert)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun AlertItem(alert: Alert, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatTimestamp(alert.timestamp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
