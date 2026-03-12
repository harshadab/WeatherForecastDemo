package com.assignment.core.uicomponents.progressbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.assignment.core.uicomponents.common.Dimens


@Composable
fun CircularProgressIndicatorBar(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("circularProgressIndicator")
                .size(Dimens.progressIndicatorSize)
        )
    }
}