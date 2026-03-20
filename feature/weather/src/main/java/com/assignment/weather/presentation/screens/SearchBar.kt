package com.assignment.weather.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.assignment.core.R
import com.assignment.core.uicomponents.common.Dimens
import com.assignment.core.utils.emptyString

@Composable
fun SearchBar(
    onQueryChange: (String) -> Unit,
    onCurrentLocationClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        var query by rememberSaveable { mutableStateOf("") }

        TextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier
                .padding(Dimens.searchBarPadding)
                .height(Dimens.searchBarHeight)
                .weight(1f),

            placeholder = {
                Text(stringResource(id = R.string.search_placeholder))
            },

            trailingIcon = {

                Row {

                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            onQueryChange(query)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search_content_description)
                        )
                    }
                }
            },

            singleLine = true,

            shape = RoundedCornerShape(Dimens.spaceS),

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onQueryChange(query)
                }
            ),

            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        CurrentLocationIconButton {
            query = emptyString()
            onCurrentLocationClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar(onQueryChange = {}){}
}