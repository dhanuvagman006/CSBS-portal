package com.dhanuvagman.csbssportal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dhanuvagman.csbssportal.R
val inriaSansRegular = FontFamily(Font(R.font.inriasansregular,FontWeight.Normal))

val Typography: Typography
    get() = Typography(
        bodyMedium = TextStyle(
            color = AquaBlue,
            fontFamily = inriaSansRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        titleLarge = TextStyle(
            color = TextWhite,
            fontFamily = inriaSansRegular,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            color = TextWhite,
            fontFamily = inriaSansRegular,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
