package com.fola.scss.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fola.data.repositryImp.CoachRepositoryImp
import com.fola.data.repositryImp.ServiceImp
import com.fola.domain.usecase.CoachUseCase
import com.fola.scss.main.coach.CoachScreen
import com.fola.scss.main.coach.CoachViewModelFactory
import com.fola.scss.main.coach.CoachViewmodel
import com.fola.scss.main.home.DashboardScreen
import com.fola.scss.main.screens.AdminScreen
import com.fola.scss.main.users.UserScreen
import com.fola.scss.ui.theme.AppTheme

sealed class MainScreen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : MainScreen("HOME", "Home", Icons.Default.Home)
    data object User : MainScreen("USERS", "Users", Icons.Default.People)
    data object Coach : MainScreen("COACHES", "coaches", Icons.Default.FitnessCenter)
    data object Admin : MainScreen("ADMIN", "Admin", Icons.Default.Settings)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Home.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(route = MainScreen.Home.route) {
                DashboardScreen()
            }
            composable(route = MainScreen.User.route) {
                UserScreen()
            }
            composable(route = MainScreen.Coach.route) {
                val coachRepo = CoachRepositoryImp()
                val serviceRepo = ServiceImp()
                val coachUseCase = CoachUseCase(
                    coachRepository = coachRepo,
                    servicesRepository = serviceRepo
                )
                val factory = CoachViewModelFactory(
                    coachUseCase = coachUseCase
                )
                val coachViewModel: CoachViewmodel = viewModel(factory = factory)
                CoachScreen(
                    viewModel = coachViewModel
                )
            }
            composable(route = MainScreen.Admin.route) {
                AdminScreen()
            }
        }
    }

}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = navBackStackEntry?.destination?.route ?: MainScreen.Home.route
    Log.d("navigation", navController.currentBackStackEntry?.destination?.route.toString())

    NavigationBar(
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        NavigationBarItem(
            selected = MainScreen.Home.route == selectedRoute,
            onClick = { navigate(navController, MainScreen.Home.route) },
            icon = { Icon(MainScreen.Home.icon, contentDescription = "") },
            label = { Text(MainScreen.Home.label) }
        )
        NavigationBarItem(
            selected = MainScreen.User.route == selectedRoute,
            onClick = { navigate(navController, MainScreen.User.route) },
            icon = { Icon(MainScreen.User.icon, contentDescription = "") },
            label = { Text(MainScreen.User.label) }
        )
        NavigationBarItem(
            selected = MainScreen.Coach.route == selectedRoute,
            onClick = { navigate(navController, MainScreen.Coach.route) },
            icon = { Icon(MainScreen.Coach.icon, contentDescription = "") },
            label = { Text(MainScreen.Coach.label) }
        )
        NavigationBarItem(
            selected = MainScreen.Admin.route == selectedRoute,
            onClick = { navigate(navController, MainScreen.Admin.route) },
            icon = {
                Icon(MainScreen.Admin.icon, contentDescription = "")
            },
            label = { Text(MainScreen.Admin.label) }
        )
    }

}

private fun navigate(navController: NavController, route: String) {

    if (navController.currentBackStackEntry?.destination?.route.toString() == route)
        return

    navController.navigate(route = route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}


@Preview
@Composable
private fun MainPrev() {
    AppTheme {
        MainApp()
    }
}

