package com.deslomator.tagtimer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.ui.SessionsScreen
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TagTimerTheme {
                val appViewModel = viewModel<AppViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            Log.d(TAG, "factory, creating viewModel")
                            val db by lazy {
                                Room.databaseBuilder(
                                    applicationContext,
                                    SessionsDatabase::class.java,
                                    "sessions.db"
                                ).build()
                            }
                            return AppViewModel(db.appDao) as T
                        }
                    }
                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by appViewModel.state.collectAsState()
                    SessionsScreen(
                        uiState,
                        appViewModel::onAction
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
