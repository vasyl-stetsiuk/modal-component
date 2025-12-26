package dev.stetsiuk.compose.modal.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.stetsiuk.compose.modal.ModalComponent
import dev.stetsiuk.compose.modal.ModalComponentState
import dev.stetsiuk.compose.modal.ProvideModalComponentHost
import dev.stetsiuk.compose.modal.rememberModalComponentState
import kotlinx.coroutines.launch
import modal.composeapp.generated.resources.Res
import modal.composeapp.generated.resources.image1
import modal.composeapp.generated.resources.image2
import modal.composeapp.generated.resources.image3
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun ModalContentPreview1() {
    val state = rememberModalComponentState(false)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.systemBarsPadding()
    ) {
        ProvideModalComponentHost(
            modifier = Modifier.weight(1f)
        ) {
            // Can be placed anywhere inside ProvideModalContentHost scope
            ModalComponent(
                state = state,
                color = Color.Transparent,
                onDismissRequest = {
                    scope.launch { state.hide() }
                }
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset {
                            IntOffset(
                                x = 0,
                                y = (200.dp - (200.dp * state.visibilityRatio)).roundToPx()
                            )
                        }
                        .alpha(state.visibilityRatio),
                    painter = painterResource(Res.drawable.image1),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                )
            }

            // App content
            Content()
        }

        Controllers(state)
    }
}

@Preview
@Composable
internal fun ModalContentPreview2() {
    val states = List(10) {
        rememberModalComponentState(false)
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.systemBarsPadding()
    ) {
        ProvideModalComponentHost(
            modifier = Modifier.weight(1f)
        ) {
            // Can be placed anywhere inside ProvideModalContentHost scope
            states.forEach { state ->
                ModalComponent(
                    state = state,
                    color = Color.Transparent,
                    onDismissRequest = {
                        scope.launch { state.hide() }
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = (200.dp - (200.dp * state.visibilityRatio)).roundToPx()
                                )
                            }
                            .alpha(state.visibilityRatio),
                        painter = painterResource(Res.drawable.image1),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }

            // App content
            Content()
        }

        Controllers2(states)
    }
}

@Composable
private fun Controllers(
    state: ModalComponentState,
    showProgress: Boolean = true
) {
    val scope = rememberCoroutineScope()
    Column(
        Modifier.padding(
            horizontal = 36.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { scope.launch { state.hide() } }
            ) { Text("Hide") }
            Button(
                onClick = { scope.launch { state.show() } }
            ) { Text("Show") }
            Spacer(Modifier.weight(1f))
            Text("${(state.visibilityRatio * 100f).toInt()}%")
        }
        if (showProgress) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = state.visibilityRatio,
                onValueChange = {
                    scope.launch { state.snapTo(it) }
                }
            )
        }
    }
}

@Composable
private fun Controllers2(
    states: List<ModalComponentState>,
) {
    val scope = rememberCoroutineScope()
    Row(
        Modifier.padding(horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = { scope.launch { states.lastOrNull { it.isVisible }?.hide() } }
        ) { Text("Hide") }
        Button(
            onClick = { scope.launch { states.firstOrNull { !it.isVisible }?.show() } }
        ) { Text("Show") }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            painter = painterResource(Res.drawable.image2),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            painter = painterResource(Res.drawable.image3),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
    }
}