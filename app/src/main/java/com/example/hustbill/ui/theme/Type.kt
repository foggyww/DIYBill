package com.example.hustbill.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.hustbill.R

object AppTypography {
    private const val baseFont = 16
    private val family = FontFamily(Font(R.raw.pingfang))

    val xLarge = TextStyle(
        fontSize = (baseFont+12).sp,
        fontFamily = family
    )

    val large : TextStyle = TextStyle(
        fontSize = (baseFont+8).sp,
        fontFamily = family
    )

    val big : TextStyle = TextStyle(
        fontSize = (baseFont+4).sp,
        fontFamily = family
    )

    val medium : TextStyle = TextStyle(
        fontSize = baseFont.sp,
        fontFamily = family
    )

    val small: TextStyle = TextStyle(
        fontSize = (baseFont-4).sp,
        fontFamily = family
    )

    val tiny : TextStyle = TextStyle(
        fontSize = (baseFont-8).sp,
        fontFamily = family
    )

    val bigTitle: TextStyle = TextStyle(
        fontSize = (baseFont+8).sp,
        fontWeight = FontWeight.W600,
        fontFamily = family
    )

    val smallTitle : TextStyle = TextStyle(
        fontSize = (baseFont).sp,
        fontWeight = FontWeight.W600,
        fontFamily = family
    )

    val smallMsgNumber: TextStyle = TextStyle(
        fontSize = (baseFont-2).sp,
        fontFamily = family
    )

    val smallMsg : TextStyle = TextStyle(
        fontSize = (baseFont-3).sp,
        fontFamily = family,
        fontWeight = FontWeight.W600
    )
}