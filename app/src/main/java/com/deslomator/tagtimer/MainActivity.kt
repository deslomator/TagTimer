package com.deslomator.tagtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.ui.SessionsScreen
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            SessionsDatabase::class.java,
            "sessions.db"
        ).build()
    }

    private val viewModel by viewModels<AppViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(db.appDao) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TagTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.state.collectAsState()
                    SessionsScreen(
                        uiState,
                        viewModel::onAction
                    )
                }
            }
        }
    }
}
