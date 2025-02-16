package com.stetsiuk.modal_component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

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
                                y = (200.dp - (200.dp * state.visibilityRatioState.value)).roundToPx()
                            )
                        }
                        .alpha(state.visibilityRatioState.value),
                    painter = painterResource(R.drawable.image1),
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
                                    y = (200.dp - (200.dp * state.visibilityRatioState.value)).roundToPx()
                                )
                            }
                            .alpha(state.visibilityRatioState.value),
                        painter = painterResource(R.drawable.image1),
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
            Text("${(state.visibilityRatioState.value * 100f).toInt()}%")
        }
        if (showProgress) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = state.visibilityRatioState.value,
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
            painter = painterResource(R.drawable.image2),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            painter = painterResource(R.drawable.image3),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
    }
}