package com.simats.dstutorai.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Topic(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color,
    val category: String
)
