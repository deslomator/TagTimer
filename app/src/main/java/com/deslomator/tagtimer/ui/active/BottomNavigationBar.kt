package com.deslomator.tagtimer.ui.active

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.navigation.screen.ActiveSessionScreen
import com.deslomator.tagtimer.navigation.screen.BottomScreens

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    sessionId: Long,
    selected: BottomScreens,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        BottomScreens.entries.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(tab.iconId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(id = tab.stringId))
                },
                selected = tab == selected,
                onClick = {
                    navController.navigate(tab.getRoute(sessionId)) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(ActiveSessionScreen(sessionId)) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.errorContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                )
            )
        }
    }
}

private const val TAG = "TopNavigationBar"