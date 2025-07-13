package com.fola.scss.main.users

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fola.scss.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserScreen(modifier: Modifier = Modifier) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SearchBar()
        }
    ) {}

}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = { _ -> },
    placeholder: String = "Search"
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { value -> onValueChange(value) },
            placeholder = { Text(text = placeholder) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    contentDescription = "Search Icon",
                    imageVector = Icons.Default.Search,
                    modifier = Modifier.size(24.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .2f),
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .6f),
                focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                cursorColor = MaterialTheme.colorScheme.primary
            ),
        )
    }
}


@Composable
fun UserItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {


    }
}


@Preview
@Composable
private fun UserScreenPrevLight() {
    AppTheme {
        UserScreen()
    }
}


@Preview
@Composable
private fun UserScreenPrevDark() {
    AppTheme(darkTheme = true) {
        UserScreen()
    }
}