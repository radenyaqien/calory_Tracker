package com.radenyaqien.onboarding_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.radenyaqien.core_ui.LocalSpasing

@Composable
fun SelectableBUtton(
    text: String,
    isSelected: Boolean,
    color: Color,
    selectedTextColor: Color,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.button,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .border(2.dp, color, RoundedCornerShape(100.dp))
            .background(
                if (isSelected) color else Color.Transparent,
                RoundedCornerShape(100.dp)
            )
            .clickable {
                onclick()
            }
            .padding(LocalSpasing.current.spaceSmall)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (isSelected) selectedTextColor else color
        )
    }
}