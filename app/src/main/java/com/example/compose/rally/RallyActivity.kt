package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.example.compose.rally.data.Accounts
import com.example.compose.rally.ui.overview.OverviewScreen

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Now you can use Accounts here
            val myAccount = Accounts("My Checking", 1000.0)
            Text(text = myAccount.name)
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val navController = rememberNavController()

        // Use currentBackStackEntryAsState correctly to get the current destination
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        // Determine the current screen by checking if the current route matches
        val currentScreen = rallyTabRowScreens.find { it.route == currentDestination?.route }
            ?: RallyDestination.Accounts  // Default to Accounts if not found

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = RallyDestination.Overview.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = RallyDestination.Overview.route) {
                    // Pass the navigation functions to OverviewScreen
                    OverviewScreen(
                        onClickSeeAllAccounts = {
                            navController.navigateSingleTopTo(RallyDestination.Accounts.route)
                        },
                        onClickSeeAllBills = {
                            navController.navigateSingleTopTo(RallyDestination.Bills.route)
                        }
                    )
                }
                composable(route = RallyDestination.Accounts.route) {
                    AccountsScreen() // Replace with actual content for Accounts
                }
                composable(route = RallyDestination.Bills.route) {
                    BillsScreen() // Replace with actual content for Bills
                }
            }
        }
    }
}

fun composable(route: Any, function: () -> Unit) {
    TODO("Not yet implemented")
}

// Define the RallyDestination enum class with the correct routes
enum class RallyDestination(val route: String) {
    Overview("overview"),
    Accounts("accounts"),
    Bills("bills")
}

// Define the extension function for NavHostController to use launchSingleTop = true
fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

// Placeholder for rallyTabRowScreens, assuming it's used for your top bar navigation
val rallyTabRowScreens: List<RallyDestination> = listOf(
    RallyDestination.Overview,
    RallyDestination.Accounts,
    RallyDestination.Bills
)

// Define composables for each screen
@Composable
fun OverviewScreen(
    onClickSeeAllAccounts: () -> Unit,
    onClickSeeAllBills: () -> Unit
) {
    androidx.compose.material.Text(text = "Overview Screen")
    // Call these functions when appropriate in the Overview screen
    androidx.compose.material.Button(onClick = onClickSeeAllAccounts) {
        androidx.compose.material.Text(text = "See All Accounts")
    }
    androidx.compose.material.Button(onClick = onClickSeeAllBills) {
        androidx.compose.material.Text(text = "See All Bills")
    }
}

@Composable
fun AccountsScreen() {
    androidx.compose.material.Text(text = "Accounts Screen")
}

@Composable
fun BillsScreen() {
    androidx.compose.material.Text(text = "Bills Screen")
}
