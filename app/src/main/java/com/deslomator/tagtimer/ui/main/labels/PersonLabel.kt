package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.MyButton
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PersonLabel(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = state.persons,
            key = { it.id }
        ) { person ->
            MyButton(
                leadingIcon = R.drawable.person,
                onLeadingClick = { onAction(LabelsTabAction.EditPersonClicked(person)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.White.contrasted()
                ),
                item = person,
                text = person.name,
                border = BorderStroke(5.dp, Color(person.color)),
                onItemClick = { onAction(LabelsTabAction.EditPersonClicked(person)) },
            )
        }

    }
}
