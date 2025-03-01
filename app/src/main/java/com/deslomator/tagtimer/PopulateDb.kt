package com.deslomator.tagtimer

import android.util.Log
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelType
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
        val tag = Label(
            name = "Tag $index",
            color = it.toHex(),
            type = LabelType.TAG.typeId
        )
        dao.upsertLabel(tag)

        val person = Label(
            name = "Tag $index",
            color = it.toHex(),
            type = LabelType.PERSON.typeId
        )

        val place = Label(
            name = "Tag $index",
            color = it.toHex(),
            type = LabelType.PLACE.typeId
        )
        dao.upsertLabel(place)
        dao.upsertLabel(person)
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