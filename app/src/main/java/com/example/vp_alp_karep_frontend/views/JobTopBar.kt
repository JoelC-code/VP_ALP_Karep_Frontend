package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JobTopBar(
    onSearch: (String) -> Unit,
    onShowAll: () -> Unit
) {
    var search by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search Job") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = { onSearch(search) }
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onShowAll) {
                Text("All Jobs")
            }
        }
    }
}