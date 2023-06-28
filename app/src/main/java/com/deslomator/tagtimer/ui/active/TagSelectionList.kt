package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
fun TagSelectionList(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = state.tags,
            key = { it.id }
        ) { tag ->
            val checked = state.preSelectedTags.map { it.tagId }.contains(tag.id)
            val onCheckedChange: (Boolean) -> Unit = { it ->
                onAction(ActiveSessionAction.SelectTagCheckedChange(tag.id, it))
            }
            MyListItem(
                leadingIcon = R.drawable.tag,
                colors = CardDefaults.cardColors(
                    contentColor = Color(tag.color).contrasted(),
                    containerColor = Color(tag.color),
                ),
                item = tag,
                shape = CutCornerShape(topStart = 20.dp),
                border = BorderStroke(1.dp, Color.LightGray),
            ) {
                Column(modifier = Modifier.weight(1.0f)) {
                    Text(tag.label)
                    Text(tag.category)
                }
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckedChange(it) }
                )
            }
        }
    }
}