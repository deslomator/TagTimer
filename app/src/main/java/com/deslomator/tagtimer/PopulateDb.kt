package com.deslomator.tagtimer

import android.util.Log
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.toHex

suspend fun populateDb (dao: AppDao) {
    colorPickerColors.forEachIndexed { index, it ->
        Log.d(TAG, "populateDb() creating sessions and tags")
        /*val session = Session(
            id = i,
            color = it.toArgb(),
            name = "Session $index"
        )
        dao.upsertSession(session)
        val sessionT = session.copy(
            id = i + 100,
            inTrash = true
        )
        dao.upsertSession(sessionT)*/
        val tag = Label.Tag(
            color = it.toHex(),
            name = "Tag $index",
        )
        dao.upsertTag(tag)
        val place = Label.Place(
            color = it.toHex(),
            name = "Place $index",
        )
        dao.upsertPlace(place)
        val person = Label.Person(
            color = it.toHex(),
            name = "Person $index",
        )
        dao.upsertPerson(person)
        /*colorPickerColors.forEachIndexed { idx, it2 ->
            val event = Event(
                sessionId = i,
                elapsedTimeMillis = (1000 * idx).toLong(),
                note = "this is a note",
                tag = "Tag $idx",
                place = "Place $idx",
                color = it2.toArgb(),
                person = "Person $idx",
            )
            dao.upsertEvent(event)
            val eventT = event.copy(
                sessionId = i,
                inTrash = true
            )
            dao.upsertEvent(eventT)

            val preselectedTag = Preselected.Tag(i, i + idx)
            dao.upsertPreSelectedTag(preselectedTag)
            val preselectedPlace = Preselected.Place(i, i + idx)
            dao.upsertPreSelectedPlace(preselectedPlace)
            val preselectedPerson = Preselected.Person(i, i + idx)
            dao.upsertPreSelectedPerson(preselectedPerson)
        }*/
    }
}

private const val TAG = "populateDb"