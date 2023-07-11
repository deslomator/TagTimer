package com.deslomator.tagtimer

import android.util.Log
import com.deslomator.tagtimer.dao.AppDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

suspend fun cleanOrphans(dao: AppDao) {
    withContext(Dispatchers.IO) {
        delay(4000)
        val oe = async { dao.clearOrphanEvents() }
        val out = async { dao.clearOrphanPreSelectedPersons() }
        val out2 = async { dao.clearOrphanPreSelectedPlaces() }
        val out3 = async { dao.clearOrphanPreSelectedTags() }
        Log.d(TAG, "dao.clearOrphanEvents(): ${oe.await()}")
        Log.d(TAG, "dao.clearOrphanPreSelectedPersons(): ${out.await()}")
        Log.d(TAG, "dao.clearOrphanPreSelectedPlaces(): ${out2.await()}")
        Log.d(TAG, "dao.clearOrphanPreSelectedTags(): ${out3.await()}")
    }
}

private const val TAG = "cleanOrphans"