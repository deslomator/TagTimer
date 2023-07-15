package com.deslomator.tagtimer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.ui.theme.Purple40
import com.deslomator.tagtimer.ui.theme.colorPickerColors

@Composable
fun ColorPicker(
    selectedColor: Color,
    onItemClick: (Color) -> Unit,
    enabled: Boolean = true
) {
    var showGrid by rememberSaveable { mutableStateOf(false) }
    AnimatedContent(showGrid ) { showG ->
        when (showG) {
            true -> {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    columns = GridCells.Adaptive(minSize = 45.dp)
                ) {
                    items(colorPickerColors) { color ->
                        val selected by remember(selectedColor) {
                            derivedStateOf { color == selectedColor }
                        }
                        ColorItem(
                            color = color,
                            onClick = {
                                showGrid = false
                                onItemClick(it)
                            },
                            selected = selected
                        )
                    }
                }
            }
            false -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    ColorItem(
                        modifier = Modifier.size(60.dp),
                        color = selectedColor,
                        onClick = { if (enabled) showGrid = true },
                        selected = true
                    )
                }
            }
        }
    }
}

@Composable
fun ColorItem(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (Color) -> Unit,
    selected: Boolean = false
) {
    val border by remember(selected) {
        derivedStateOf {
            if (selected) Color.Gray else Color.Transparent
        }
    }
    Box(
        modifier = modifier
            .size(45.dp)
            .border(1.dp, border, RoundedCornerShape(30))
            .padding(4.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick(color) },
            shape = RoundedCornerShape(30),
            colors = CardDefaults.cardColors(
                containerColor = color,
            ),
        ) { }
    }
}

@Composable
@Preview
fun ColorPickerPreview() {
    ColorPicker(
        selectedColor = Purple40,
        onItemClick = {}
    )
}
