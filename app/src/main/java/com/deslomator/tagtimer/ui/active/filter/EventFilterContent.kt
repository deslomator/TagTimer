package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.active.PreSelectedPersonsList
import com.deslomator.tagtimer.ui.active.PreSelectedPlacesList

@Composable
fun EventFilterContent(
    paddingValues: PaddingValues,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    /*
    get places that are preselected AND actually used as Event
     */
    val places by remember {
        derivedStateOf {
            state.places
                .filter { place ->
                    state.events.map { it.place }.distinct().contains(place.name)
                }
        }
    }
    /*
    get places that are preselected AND actually used as Event
     */
    val persons by remember {
        derivedStateOf {
            state.persons
                .filter { person ->
                    state.events.map { it.person }.distinct().contains(person.name)
                }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {

            PreSelectedPlacesList(
                places = places,
                currentPlace = state.currentPlaceName,
                onAction = onAction
            )
            PreSelectedPersonsList(
                persons = persons,
                currentPerson = state.currentPersonName,
                onAction = onAction
            )
        }
    }
}

private const val TAG = "ActiveSessionContent"