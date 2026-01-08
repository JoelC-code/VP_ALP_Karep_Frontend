package com.example.vp_alp_karep_frontend.views.CompanyViews

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Professional color scheme - Balanced for eye comfort
private val AccentGold = Color(0xFFD4AF37)

@Composable
fun CircleLoadingTemplate(
    modifier: Modifier = Modifier,
    color: Color = AccentGold,
    trackColor: Color = Color.Transparent
) {
    CircularProgressIndicator(
        modifier = modifier.size(40.dp),
        color = color,
        trackColor = trackColor,
        strokeWidth = 4.dp
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CircleLoadingPreview() {
    CircleLoadingTemplate(
        modifier = Modifier.size(40.dp),
        color = AccentGold,
        trackColor = Color.Transparent
    )
}