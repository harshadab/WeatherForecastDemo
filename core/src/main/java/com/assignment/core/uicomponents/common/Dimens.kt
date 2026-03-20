package com.assignment.core.uicomponents.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.assignment.core.R

object Dimens {

    val spaceXXS: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_xxs)

    val spaceXS: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_xs)

    val spaceS: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_s)

    val spaceM: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_m)

    val spaceL: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_l)

    val spaceXL: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_xl)

    val spaceXXL: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.space_xxl)

    val iconSizeLarge: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.icon_size_large)

    val iconSizeMedium: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.icon_size_medium)

    val iconSizeSmall: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.icon_size_small)

    val progressIndicatorSize: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.progress_indicator_size)

    val searchBarPadding: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.search_bar_padding)

    val searchBarHeight: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.search_bar_height)

    val hourCardItemHeight: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.hour_card_item_height)

    val weatherIconSize: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.weather_icon_size)


}