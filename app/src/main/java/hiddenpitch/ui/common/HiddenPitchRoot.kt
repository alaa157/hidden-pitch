package hiddenpitch.ui.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hiddenpitch.AppContainer
import hiddenpitch.ui.add.AddPlaceScreen
import hiddenpitch.ui.detail.DetailScreen
import hiddenpitch.ui.home.HomeScreen
import hiddenpitch.ui.myplaces.MyPlacesScreen
import hiddenpitch.ui.settings.SettingsScreen

@Composable
fun HiddenPitchRoot(container: AppContainer) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetail = { navController.navigate(Routes.detail(it)) },
                onOpenAdd = { container.authGate.openProtectedDestination { navController.navigate(Routes.ADD_PLACE) } },
                onOpenMyPlaces = { container.authGate.openProtectedDestination { navController.navigate(Routes.MY_PLACES) } },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                factory = container.viewModelFactory
            )
        }
        composable(Routes.DETAIL, arguments = listOf(navArgument("placeId") { type = NavType.StringType })) { entry ->
            DetailScreen(entry.arguments?.getString("placeId").orEmpty(), { navController.popBackStack() }, container.viewModelFactory)
        }
        composable(Routes.ADD_PLACE) {
            AddPlaceScreen({ navController.popBackStack() }, container.viewModelFactory)
        }
        composable(Routes.MY_PLACES) {
            MyPlacesScreen({ navController.popBackStack() }, container.viewModelFactory)
        }
        composable(Routes.SETTINGS) {
            SettingsScreen({ navController.popBackStack() }, container.viewModelFactory)
        }
    }
}
