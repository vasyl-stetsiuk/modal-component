package dev.stetsiuk.compose.modal.test

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform