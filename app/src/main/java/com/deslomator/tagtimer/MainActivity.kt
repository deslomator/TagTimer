package com.deslomator.tagtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.ui.Screen
import com.deslomator.tagtimer.ui.sessions.SessionsScreen
import com.deslomator.tagtimer.ui.sessionsTrash.SessionsTrashScreen
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.viewmodel.AppViewModel
import com.deslomator.tagtimer.viewmodel.SessionsScreenViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTrashViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TagTimerTheme {
                val db by lazy {
                    Room.databaseBuilder(
                        applicationContext,
                        SessionsDatabase::class.java,
                        "sessions.db"
                    ).build()
                }
                val appViewModel = viewModel<AppViewModel>()
                val sessionsScreenViewModel = getSessionsScreenViewModel(
                    db = db
                )
                val sessionsTrashViewModel = getSessionsTrashViewModel(
                    db = db
                )

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appState by appViewModel.state.collectAsState()
                    val sessionsScreenState by sessionsScreenViewModel.state.collectAsState()
                    val sessionsTrashState by sessionsTrashViewModel.state.collectAsState()

                    when (appState.currentScreen) {
                        Screen.SESSIONS -> {
                            SessionsScreen(
                                appViewModel::onAction,
                                sessionsScreenState,
                                sessionsScreenViewModel::onAction
                            )
                        }
                        Screen.SESSIONS_TRASH -> {
                            SessionsTrashScreen(
                                appViewModel::onAction,
                                sessionsTrashState,
                                sessionsTrashViewModel::onAction
                            )
                        }
                        Screen.CURRENTSESSION -> TODO()
                        Screen.TAGS -> TODO()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
private fun getSessionsScreenViewModel(db: SessionsDatabase): SessionsScreenViewModel {
    return viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SessionsScreenViewModel(db.appDao) as T
            }
        }
    )
}
@Composable
private fun getSessionsTrashViewModel(db: SessionsDatabase): SessionsTrashViewModel {
    return viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SessionsTrashViewModel(db.appDao) as T
            }
        }
    )
}