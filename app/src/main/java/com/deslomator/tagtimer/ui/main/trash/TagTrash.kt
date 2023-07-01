package com.deslomator.tagtimer.ui.main.trash

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.MyButton
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.contrasted
import kotlinx.coroutines.CoroutineScope

@Composable
fun TagTrash(
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
            items = state.tags,
            key = { it.id }
        ) { tag ->
            MyButton(
                leadingIcon = R.drawable.restore_from_trash,
                onLeadingClick = {
                    onAction(TrashTabAction.RestoreTagClicked(tag))
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.tag_restored)
                    )
                },
                trailingIcon = R.drawable.delete_forever,
                onTrailingClick = {
                    onAction(
                        TrashTabAction.DeleteTagClicked(tag))
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.tag_deleted)
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(tag.color),
                    contentColor = Color(tag.color).contrasted()
                ),
                item = tag,
                text = tag.label,
                border = BorderStroke(1.dp, Color.LightGray),
            )
        }
    }
}