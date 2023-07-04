package com.deslomator.tagtimer

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.navigation.AppNavHost
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            TagTimerTheme {
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) { cleanOrphans(appDao) }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

private suspend fun cleanOrphans(dao: AppDao) {
    delay(4000)
    val oe = dao.clearOrphanEvents()
    val out = dao.clearOrphanPreSelectedPersons()
    val out2 = dao.clearOrphanPreSelectedPlaces()
    val out3 = dao.clearOrphanPreSelectedTags()
    Log.d(TAG, "dao.clearOrphanEvents(): $oe")
    Log.d(TAG, "dao.clearOrphanPreSelectedPersons(): $out")
    Log.d(TAG, "dao.clearOrphanPreSelectedPlaces(): $out2")
    Log.d(TAG, "dao.clearOrphanPreSelectedTags(): $out3")
}

private const val TAG = "MainActivity"