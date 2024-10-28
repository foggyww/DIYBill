package com.example.hustbill.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

object AppTypography {
    private const val baseFont = 16

    val xLarge = TextStyle(
        fontSize = (baseFont+12).sp,
    )

    val large : TextStyle = TextStyle(
        fontSize = (baseFont+8).sp,
    )

    val big : TextStyle = TextStyle(
        fontSize = (baseFont+4).sp,
    )

    val medium : TextStyle = TextStyle(
        fontSize = baseFont.sp
    )

    val small: TextStyle = TextStyle(
        fontSize = (baseFont-4).sp
    )

    val tiny : TextStyle = TextStyle(
        fontSize = (baseFont-8).sp,
    )
}