package cz.cvut.smarthub.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.cvut.smarthub.SmartHubScreen
import cz.cvut.smarthub.R
import cz.cvut.smarthub.ui.theme.SmartHubTheme

@Composable
fun DevicesBottomBar(navController: NavHostController) {
    val items = listOf(
        Triple(R.drawable.baseline_dashboard_24, stringResource(R.string.dashboard_tab_title), SmartHubScreen.Dashboard.name),
        Triple(R.drawable.baseline_device_hub_24, stringResource(R.string.devices_tab_title), SmartHubScreen.Devices.name),
        Triple(R.drawable.baseline_add_alert_24, stringResource(R.string.alerts_tab_title), SmartHubScreen.Alerts.name),
        Triple(R.drawable.baseline_linear_scale_24, stringResource(R.string.scenes_tab_title), SmartHubScreen.Scenes.name)
    )

    NavigationBar {
        val currentRoute = navController.currentDestination?.route

        items.forEach {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = it.first), contentDescription = it.second) },
                label = { Text(it.second) },
                selected = currentRoute == it.third,
                onClick = {
                    navController.navigate(it.third) {
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // Save the state of all destinations before popping them
                        }
                        // Restore state when navigating to a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PreviewDevicesBottomBar() {
    SmartHubTheme {
        Scaffold(
            bottomBar = {
                DevicesBottomBar(navController = rememberNavController())
            }
        ) {
        }
    }
}