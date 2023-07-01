package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.ui.theme.contrasted
import androidx.compose.foundation.lazy.grid.items

@Composable
fun UsedTagsList(
    tags: List<Tag>,
    currentTag: String,
    onAction: (ActiveSessionAction) -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            columns = GridCells.Adaptive(100.dp),
        ) {
            items(
                items = tags,
                key = { it.id }
            ) { tag ->
                TextButton(
                    modifier = Modifier
                        .weight(1F)
                        .alpha(if (currentTag == tag.label) 1F else 0.4F),
                    onClick = { onAction(ActiveSessionAction.UsedTagClicked(tag.label)) },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color(tag.color).contrasted(),
                        containerColor = Color(tag.color),
                    ),
                    border = BorderStroke(1.dp, Color.LightGray),
                ) {
                    Icon(
                        painterResource(id = R.drawable.tag),
                        contentDescription = null
                    )
                    Text(text = tag.label,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}