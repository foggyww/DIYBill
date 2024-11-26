package com.example.diybill.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)
val CardShapes = Shapes(
    extraSmall = RoundedCornerShape(5.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(15.dp),
    large = RoundedCornerShape(20.dp)
)
val RoundedShapes = Shapes(
    small = RoundedCornerShape(50),
    medium = RoundedCornerShape(50),
    large = RoundedCornerShape(50)
)
val CardShapesTopHalf = Shapes(
	small = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
	medium = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
	large = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
)