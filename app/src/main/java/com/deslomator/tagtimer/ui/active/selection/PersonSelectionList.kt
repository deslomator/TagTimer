package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PersonSelectionList(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = state.persons,
            key = { it.id }
        ) { person ->
            val checked = state.preSelectedPersons.map { it.personId }.contains(person.id)
            val onCheckedChange: (Boolean) -> Unit = { it ->
                onAction(ActiveSessionAction.SelectPersonCheckedChange(person.id, it))
            }
            MyListItem(
                leadingIcon = R.drawable.person,
                colors = CardDefaults.cardColors(
                    contentColor = Color.White.contrasted(),
                    containerColor = Color.White,
                ),
                item = person,
                shape = CutCornerShape(topStart = 20.dp),
                border = BorderStroke(5.dp, Color(person.color)),
            ) {
                Column(modifier = Modifier.weight(1.0f)) {
                    Text(person.name)
                }
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckedChange(it) }
                )
            }
        }
    }
}