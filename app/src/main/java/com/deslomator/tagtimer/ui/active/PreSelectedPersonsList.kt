package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PreSelectedPersonsList(
    persons: List<Person>,
    currentPerson: String,
    onAction: (ActiveSessionAction) -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        persons.forEach { person ->
            TextButton(
                modifier = Modifier
                    .weight(1F)
                    .alpha(if (currentPerson == person.name) 1F else 0.4F),
                onClick = { onAction(ActiveSessionAction.PreSelectedPersonClicked(person.name)) },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White.contrasted(),
                    containerColor = Color.White
                ),
                border = BorderStroke(3.dp, Color(person.color))
            ) {
                Icon(
                    painterResource(id = R.drawable.person),
                    contentDescription = null
                )
                Text(
                    text = person.name,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}