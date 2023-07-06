package com.deslomator.tagtimer

import android.util.Log
import com.deslomator.tagtimer.dao.AppDao
import kotlinx.coroutines.delay

suspend fun cleanOrphans(dao: AppDao) {
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

private const val TAG = "cleanOrphans"