package com.example.truecallercopy.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewDialer() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = {},
            Modifier.fillMaxWidth(),
            placeholder = { Text("Enter phone number") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {}) { Text("Call") }
            Button(onClick = {}) { Text("Clear") }
        }
    }
}
