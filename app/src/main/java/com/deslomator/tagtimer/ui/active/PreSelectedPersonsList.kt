package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PreSelectedPersonsList(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyRow {
        items(
            items = state.persons.filter { person ->
                state.preSelectedPersons.map { it.personId }.contains(person.id)
            },
            key = { it.id }
        ) { person ->
            MyListItem(
                leadingIcon = R.drawable.person,
                onLeadingClick = { onAction(ActiveSessionAction.PreSelectedPersonClicked(person.name)) },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.White.contrasted()
                ),
                item = person,
                shape = CutCornerShape(topStart = 20.dp),
                border = BorderStroke(5.dp, Color(person.color)),
                onItemClick = { onAction(ActiveSessionAction.PreSelectedPersonClicked(person.name)) },
            ) { item ->
                Text(item.name)
            }
        }
    }
}