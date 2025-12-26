package dev.stetsiuk.compose.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ModalComponentData @OptIn(ExperimentalUuidApi::class) constructor(
    val state: ModalComponentState,
    val id: String = Uuid.random().toString(),
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val onDismissRequest: () -> Unit,
    val configs: ModalComponentHostConfig = ModalComponentHostConfig.default(),
    val content: @Composable () -> Unit,
)

data class ModalComponentHostConfig(
    val contentAlignment: Alignment,
    val backgroundBlur: Dp,
    val backgroundTint: Color,
    val backgroundScaleRatio: Float,
) {
    companion object {
        fun default(): ModalComponentHostConfig {
            return ModalComponentHostConfig(
                contentAlignment = Alignment.BottomCenter,
                backgroundBlur = 12.dp,
                backgroundTint = Color.Black.copy(0.1f),
                backgroundScaleRatio = 0.95f,
            )
        }
    }
}