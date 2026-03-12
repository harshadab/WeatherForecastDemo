package com.assignment.weather.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.assignment.core.R
import com.assignment.core.uicomponents.common.Dimens
import com.assignment.core.uicomponents.common.TypographyDefault

@Composable
fun LocationHeader(city: String) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = null,
            tint = Color.White
        )

        Spacer(Modifier.width(Dimens.spaceS))

        Text(
            text = city,
            style = TypographyDefault().fontTitle1,
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocationHeaderPreview() {
    LocationHeader(city = "London")
}