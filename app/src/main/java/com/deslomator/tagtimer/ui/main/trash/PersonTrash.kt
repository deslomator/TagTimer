package com.deslomator.tagtimer.ui.main.trash

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.contrasted
import kotlinx.coroutines.CoroutineScope

@Composable
fun PersonTrash(
    state: TrashTabState,
    onAction: (TrashTabAction) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.persons,
            key = { it.id }
        ) { person ->
            MyListItem(
                colors = CardDefaults.cardColors(
                    contentColor = Color.White.contrasted(),
                    containerColor = Color.White,
                ),
                item = person,
                shape = CutCornerShape(topStart = 20.dp),
                border = BorderStroke(5.dp, Color(person.color)),
                leadingIcon = R.drawable.restore_from_trash,
                onLeadingClick = {
                    onAction(TrashTabAction.RestorePersonClicked(person))
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.person_restored)
                    )
                },
                trailingIcon = R.drawable.delete_forever,
                onTrailingClick = {
                    onAction(
                        TrashTabAction.DeletePersonClicked(person))
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.person_deleted)
                    )
                },
            ) { item ->
                Column {
                    Text(item.name)
                }
            }
        }
    }
}