package com.deslomator.tagtimer.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.sessions.SessionsScreenContent
import com.deslomator.tagtimer.ui.sessions.SessionsTopBar
import com.deslomator.tagtimer.ui.sessionsTrash.SessionsTrashContent

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = BottomNavigationScreen.Main.route,
    sessionsScreenState: SessionsScreenState,
    onSessionsAction: (SessionsScreenAction) -> Unit,
    sessionsTrashState: SessionsTrashState,
    onSessionsTrashAction: (SessionsTrashAction) -> Unit,
) {

    /*Scaffold(
        topBar = { SessionsTopBar(
            onNewSessionClick = { onSessionsAction(SessionsScreenAction.AddNewSessionClicked) },
            onManageTagsClick = { onSessionsAction(SessionsScreenAction.ManageTagsClicked) },
            onGoToTrashClick = {
                onSessionsAction(SessionsScreenAction.HideSnackbar)
//                navController.navigate("sessionsTrash")
                navController.navigate("hello")
            },
        ) },
        bottomBar = {
            NavigationBar {
                tabItems.forEach { tab ->
                    val selected = selectionMap.getOrDefault(tab, false)
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(tab.icon),
                                contentDescription = tab.route
                            )
                        },
                        label = {
                            Text(stringResource(id = tab.stringId))
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->*/

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BottomNavigationScreen.Main.route,
    ) {
        composable(
            route = BottomNavigationScreen.Main.route,
        ) {
            val navController2: NavHostController = rememberNavController()

            val tabItems = listOf(
                BottomNavigationScreen.Sessions,
//        BottomNavigationScreens.Tags,
                BottomNavigationScreen.Trash,
            )
            val backStackEntry = navController2.currentBackStackEntryAsState()

            val snackbarHostState = remember { SnackbarHostState() }
            val context = LocalContext.current
            if (sessionsScreenState.showSnackbar) {
                LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching snackbar")
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.session_sent_to_trash),
                        duration = SnackbarDuration.Short
                    )
                    onSessionsAction(SessionsScreenAction.HideSnackbar)
                }
            }
            Scaffold(
                topBar = {
                    SessionsTopBar(
                        onNewSessionClick = { onSessionsAction(SessionsScreenAction.AddNewSessionClicked) },
                        onManageTagsClick = { onSessionsAction(SessionsScreenAction.ManageTagsClicked) },
                        onGoToTrashClick = {
                            onSessionsAction(SessionsScreenAction.HideSnackbar)
//                navController.navigate("sessionsTrash")
                            navController.navigate("hello")
                        },
                    )
                },
                bottomBar = {
                    NavigationBar {
                        tabItems.forEach { tab ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(tab.icon),
                                        contentDescription = tab.route
                                    )
                                },
                                label = {
                                    Text(stringResource(id = tab.stringId))
                                },
                                selected = tab.route == backStackEntry.value?.destination?.route,
                                onClick = {
                                    navController2.navigate(tab.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController2.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                content = { pdv ->

                    NavHost(
                        modifier = modifier,
                        navController = navController2,
                        startDestination = BottomNavigationScreen.Sessions.route,
                    ) {
                        composable(BottomNavigationScreen.Sessions.route) {
                            SessionsScreenContent(
                                paddingValues = pdv,
                                state = sessionsScreenState,
                                onAction = onSessionsAction
                            )
                        }
                        composable(BottomNavigationScreen.Trash.route) {
                            SessionsTrashContent(
                                paddingValues = pdv,
                                state = sessionsTrashState,
                                onAction = onSessionsTrashAction
                            )
                        }

                    }

                }
            )

        }




        composable("hello") {
            Column {
                Hello()
            }
        }
    }
}
//}

@Composable
fun Hello() {
    Text(text = "hello hello")
}
