@file:OptIn(ExperimentalMaterial3Api::class)

package com.stefang.modern.swapi.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScaffoldScreen(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    snackBarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        color = Purple40
                    )
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                        content = actions
                    )
                }
            )
        },
        snackbarHost = {
            snackBarHostState?.let { SnackbarHost(it) }
        },
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun ScaffoldPreview() {
    MyApplicationTheme {
        ScaffoldScreen(
            title = "My Application",
            actions = {},
            content = {}
        )
    }
}
