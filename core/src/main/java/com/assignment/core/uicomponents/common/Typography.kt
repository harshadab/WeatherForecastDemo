package com.assignment.core.uicomponents.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class Typography(
    val fontRobotoRegular: TextStyle,
    val fontRobotoRegular7: TextStyle,
    val fontRobotoRegular10: TextStyle,
    val fontRobotoRegularBold: TextStyle,
    val fontSuperHeader: TextStyle,
    val fontSuperLarge: TextStyle,
    val fontTitle1: TextStyle,
    val fontTitle2: TextStyle,
    val fontTitle3: TextStyle,
    val fontButton: TextStyle,
    val fontBody: TextStyle,
    val fontStatus: TextStyle,
    val fontCaption1: TextStyle,
    val fontCaption2: TextStyle,
)

private val fontRobotoRegular =
    TextStyle(fontFamily = FontFamily.SansSerif, fontStyle = FontStyle.Normal)

private val fontRobotoRegularBold =
    fontRobotoRegular.copy(fontWeight = FontWeight.Bold)

@Composable
fun TypographyDefault(): Typography {
    return Typography(
        fontRobotoRegular = fontRobotoRegular,
        fontRobotoRegular7 = fontRobotoRegular.copy(fontSize = 7.sp),
        fontRobotoRegular10 = fontRobotoRegular.copy(fontSize = 10.sp),
        fontRobotoRegularBold = fontRobotoRegularBold,
        fontSuperHeader = fontRobotoRegularBold.copy(fontSize = 40.sp),
        fontTitle1 = fontRobotoRegularBold.copy(fontSize = 26.sp),
        fontTitle2 = fontRobotoRegularBold.copy(fontSize = 20.sp),
        fontTitle3 = fontRobotoRegularBold.copy(fontSize = 16.sp),
        fontButton = fontRobotoRegularBold.copy(fontSize = 16.sp),
        fontBody = fontRobotoRegular.copy(fontSize = 16.sp),
        fontStatus = fontRobotoRegular.copy(fontSize = 16.sp),
        fontCaption1 = fontRobotoRegular.copy(fontSize = 14.sp),
        fontCaption2 = fontRobotoRegular.copy(fontSize = 14.sp),
        fontSuperLarge =  fontRobotoRegularBold.copy(fontSize = 90.sp),
    )
}
