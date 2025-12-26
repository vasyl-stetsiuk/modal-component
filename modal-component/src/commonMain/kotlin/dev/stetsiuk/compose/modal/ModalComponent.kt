package dev.stetsiuk.compose.modal

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Surface params
@OptIn(ExperimentalUuidApi::class)
@Composable
fun ModalComponent(
    state: ModalComponentState,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    shape: Shape = RectangleShape,
    contentColor: Color = contentColorFor(color),
    shadowElevation: Dp = 0.dp,
    tonalElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    hostConfigs: ModalComponentHostConfig = ModalComponentHostConfig.default(),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    val host = LocalModalComponentHostState.current
    val id = rememberSaveable { Uuid.random().toString() }
    val hostContent = @Composable {
        Surface(
            modifier = modifier,
            color = color,
            contentColor = contentColor,
            border = border,
            shape = shape,
            shadowElevation = shadowElevation,
            tonalElevation = tonalElevation
        ) {
            content.invoke()
        }
    }
    DisposableEffect(Unit) {
        val data = ModalComponentData(
            id = id,
            state = state,
            content = hostContent,
            configs = hostConfigs,
            onDismissRequest = onDismissRequest,
            dismissOnClickOutside = dismissOnClickOutside,
            dismissOnBackPress = dismissOnBackPress
        )
        host.add(data)
        onDispose { host.remove(data) }
    }
}