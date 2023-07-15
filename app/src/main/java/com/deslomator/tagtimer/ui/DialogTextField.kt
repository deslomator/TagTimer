package com.deslomator.tagtimer.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DialogTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholder: Int? = null,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true
) {
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
        TextField(
            modifier = modifier.weight(1F),
            value = value,
            onValueChange = onValueChange,
            placeholder = { placeholder?.let { Text(text = (stringResource(id = it))) } },
            enabled = enabled,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )
    }
}