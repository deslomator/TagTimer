package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.deslomator.tagtimer.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LabelsTopBar(
    onNewTagClick: () -> Unit,
    onNewPersonClick: () -> Unit,
    onNewPlaceClick: () -> Unit,
    pagerState: PagerState,

    ) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            AnimatedContent(pagerState.currentPage) {
                when (it) {
                    0 -> {
                        IconButton(
                            onClick = onNewTagClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_tag),
                                contentDescription = stringResource(id = R.string.new_tag)
                            )
                        }
                    }
                    1 -> {
                        IconButton(
                            onClick = onNewPersonClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_person),
                                contentDescription = stringResource(id = R.string.new_person)
                            )
                        }
                    }
                    else -> {
                        IconButton(
                            onClick = onNewPlaceClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_place),
                                contentDescription = stringResource(id = R.string.new_place)
                            )
                        }
                    }
                }
            }
        }
    )
}
