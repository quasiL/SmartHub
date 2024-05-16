package cz.cvut.smarthub

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.smarthub.ui.screens.alerts.AlertsScreen
import cz.cvut.smarthub.ui.screens.dashboard.DashboardScreen
import cz.cvut.smarthub.ui.screens.dashboard.EnergyUsageScreen
import cz.cvut.smarthub.ui.screens.dashboard.SecurityStatusScreen
import cz.cvut.smarthub.ui.screens.devices.DeviceDetailScreen
import cz.cvut.smarthub.ui.screens.devices.DevicesScreen
import cz.cvut.smarthub.ui.screens.devices.EditDeviceDetailScreen
import cz.cvut.smarthub.ui.screens.scenes.ScenesScreen
import cz.cvut.smarthub.ui.screens.scenes.SceneDetailScreen
import cz.cvut.smarthub.ui.screens.scenes.EditSceneDetailScreen
import cz.cvut.smarthub.ui.theme.SmartHubTheme

enum class SmartHubScreen {
    Devices,
    DeviceDetail,
    EditDeviceDetail,
    Alerts,
    Dashboard,
    Scenes,
    SceneDetail,
    EditSceneDetail,
    EnergyUsage,
    SecurityStatus
}

@Composable
fun SmartHubApp() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SmartHubScreen.Devices.name,
        modifier = Modifier
    ) {
        composable(
            route = SmartHubScreen.Dashboard.name
        ) {
            DashboardScreen(navController)
        }
        composable(route = SmartHubScreen.Devices.name) {
            DevicesScreen(navController)
        }
        composable(
            route = SmartHubScreen.DeviceDetail.name+"/{ID}",
            arguments = listOf(navArgument("ID") { type = NavType.LongType })
        ) {
            DeviceDetailScreen(navController)
        }
        composable(
            route = "${SmartHubScreen.EditDeviceDetail.name}/{ID}",
            arguments = listOf(navArgument("ID") { type = NavType.LongType })
        ) {
            EditDeviceDetailScreen(navController)
        }
        composable(
            route = SmartHubScreen.Alerts.name
        ) {
            AlertsScreen(navController)
        }
        composable(
            route = SmartHubScreen.Scenes.name
        ) {
            ScenesScreen(navController)
        }
        composable(
            route = SmartHubScreen.SceneDetail.name+"/{ID}",
            arguments = listOf(navArgument("ID") { type = NavType.LongType })
        ) {
            SceneDetailScreen(navController)
        }
        composable(
            route = "${SmartHubScreen.EditSceneDetail.name}/{ID}",
            arguments = listOf(navArgument("ID") { type = NavType.LongType })
        ) {
            EditSceneDetailScreen(navController)
        }
        composable(route = SmartHubScreen.EnergyUsage.name) {
            EnergyUsageScreen(navController)
        }
        composable(route = SmartHubScreen.SecurityStatus.name) {
            SecurityStatusScreen(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaygroundsAppPreview() {
    SmartHubTheme {
        SmartHubApp()
    }
}