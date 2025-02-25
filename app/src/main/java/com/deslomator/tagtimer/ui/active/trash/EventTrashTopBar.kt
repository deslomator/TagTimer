package com.deslomator.tagtimer.ui.active.trash

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.ui.theme.topBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTrashTopBar(
    title: String,
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "navigate back"
                )
            }
        },
        title = { Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        actions = {
            Icon(
                painterResource(id = R.drawable.delete),
                contentDescription = "event trash"
            )
        },
        colors = topBarColors()
    )
}
