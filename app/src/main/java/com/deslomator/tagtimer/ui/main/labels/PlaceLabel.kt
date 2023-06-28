package com.deslomator.tagtimer.ui.main.labels

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.contrasted
import kotlinx.coroutines.CoroutineScope

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PlaceLabel(
    state: LabelsTabState,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context,
    onAction: (LabelsTabAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.places,
            key = { it.name }
        ) { place ->
            SwipeableListItem(
                dismissDirection = DismissDirection.StartToEnd,
                onDismiss = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.place_sent_to_trash)
                    )
                    onAction(LabelsTabAction.TrashPlaceSwiped(place))
                },
                dismissColor = Pink80
            ) {
                MyListItem(
                    modifier = Modifier
                        .clip(CutCornerShape(topStart = 20.dp))
                        .border(1.dp, Color.LightGray, CutCornerShape(topStart = 20.dp)),
                    leadingIcon = R.drawable.place,
                    onLeadingClick = { onAction(LabelsTabAction.EditPlaceClicked(place)) },
                    colors = ListItemDefaults.colors(
                        leadingIconColor = Color(place.color).contrasted(),
                        headlineColor = Color(place.color).contrasted(),
                        trailingIconColor = Color(place.color).contrasted(),
                        containerColor = Color(place.color),
                    ),
                    item = place,
                    onItemClick = { onAction(LabelsTabAction.EditPlaceClicked(place)) },
                ) { item ->
                    Text(item.name)
                }
            }
        }

    }
}