package com.deslomator.tagtimer

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.navigation.AppNavHost
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class TagTimerApp: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appDao: AppDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var orphansCleared by rememberSaveable { mutableStateOf(false) }
            TagTimerTheme {
                LaunchedEffect(Unit) {
                    if (!orphansCleared) {
                        withContext(Dispatchers.IO) { cleanOrphans(appDao) }
                    }
                    orphansCleared = true
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                    IntentProcessor(intent = intent, activity = this, appDao = appDao)
                }
            }
        }
    }
}

private const val TAG = "MainActivity"
