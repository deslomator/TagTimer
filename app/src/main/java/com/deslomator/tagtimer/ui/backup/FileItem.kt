package com.deslomator.tagtimer.ui.backup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import java.io.File

@Composable
fun FileItem(
    file: File,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    onRestoreClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by remember {
        derivedStateOf { if (expanded) 180F else 0F }
    }
    val animRotation by animateFloatAsState(targetValue = rotation)
    Column(
        modifier = Modifier
            .clickable { expanded = !expanded }
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(9.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = file.name,
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                modifier = Modifier.rotate(animRotation),
                painter = painterResource(id = R.drawable.expand_more),
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = onRestoreClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.restore),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = onSaveClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = onShareClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_forever),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

