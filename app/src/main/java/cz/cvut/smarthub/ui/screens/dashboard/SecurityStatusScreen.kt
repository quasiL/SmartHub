package cz.cvut.smarthub.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.cvut.smarthub.ui.components.BasicAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityStatusScreen(navController: NavHostController) {
    var systemArmed by remember { mutableStateOf(true) }
    val sensors = listOf(
        SensorData("Front Door", true),
        SensorData("Back Door", false),
        SensorData("Living Room Window", true),
        SensorData("Kitchen Window", true)
    )
    var sensorStates by remember { mutableStateOf(sensors) }

    Scaffold(
        topBar = {
            Column {
                BasicAppBar(
                    title = "Security Status",
                    canNavigateBack = true,
                    onNavigateBack = { navController.popBackStack() },
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Security System",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "System Status",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Switch(
                    checked = systemArmed,
                    onCheckedChange = { systemArmed = it },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Sensors",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                sensorStates.forEachIndexed { index, sensor ->
                    SensorItem(
                        name = sensor.name,
                        isActive = sensor.isActive,
                        onToggle = { isActive ->
                            sensorStates = sensorStates.toMutableList().apply {
                                set(index, sensor.copy(isActive = isActive))
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        systemArmed = !systemArmed
                        sensorStates = sensorStates.map { it.copy(isActive = systemArmed) }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(if (systemArmed) "Disarm System" else "Arm System")
                }
            }
        }
    )
}

@Composable
fun SensorItem(name: String, isActive: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isActive,
            onCheckedChange = onToggle,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
