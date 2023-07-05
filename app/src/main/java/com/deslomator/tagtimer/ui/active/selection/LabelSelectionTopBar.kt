package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.deslomator.tagtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectionTopBar(
    title: String,
    onBackClicked: () -> Unit,
    currentPage: Int,
    onAddTagClick: () -> Unit,
    onAddPersonClick: () -> Unit,
    onAddPlaceClick: () -> Unit,
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
            when (currentPage) {
                0 -> {
                    IconButton(
                        onClick = onAddTagClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_tag),
                            contentDescription = stringResource(id = R.string.add_tag)
                        )
                    }
                }
                1 -> {
                    IconButton(
                        onClick = onAddPersonClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_person),
                            contentDescription = stringResource(id = R.string.add_person)
                        )
                    }
                }
                else -> {
                    IconButton(
                        onClick = onAddPlaceClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_place),
                            contentDescription = stringResource(id = R.string.add_place)
                        )
                    }
                }
            }
        }
    )
}
