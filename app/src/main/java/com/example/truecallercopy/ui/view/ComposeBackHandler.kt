package com.example.truecallercopy.ui.view

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun CustomBackHandler(onClose: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onClose() // Call your custom logic to close the overlay
            }
        }
        backDispatcher?.addCallback(callback)

        onDispose {
            callback.remove() // Clean up the callback
        }
    }
}