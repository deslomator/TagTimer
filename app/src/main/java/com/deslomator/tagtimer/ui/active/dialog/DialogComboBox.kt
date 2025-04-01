package com.deslomator.tagtimer.ui.active.dialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Label

@Composable
fun DialogComboBox(
    modifier: Modifier = Modifier,
    label: Label? = null,
    onValueChange: (Label) -> Unit,
    @DrawableRes icon: Int? = null,
    @StringRes noLabel: Int,
    activeLabels: List<Label>
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf(label?.name) }
    val labels = activeLabels.toMutableList().also {
        it.add(0, Label(id = null, name = stringResource(noLabel)))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                modifier = modifier.size(30.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Column(modifier = modifier.weight(1F)) {
            Box(modifier = modifier.padding(5.dp)) {
                TextButton(
                    onClick = { expanded = true }
                ) {
                    Text(text = name ?: labels[0].name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    labels.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.name) },
                            onClick = {
                                expanded = false
                                name = it.name
                                onValueChange(it)
                            }
                        )
                    }
                }
            }
            HorizontalDivider(color = Color.Black)
        }
    }
}