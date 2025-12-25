package dev.stetsiuk.compose.modal.test

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TestComposeMultiplatformApp",
    ) {
        App()
    }
}