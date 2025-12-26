package dev.stetsiuk.compose.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

internal data class Params(
    val blur: Dp,
    val scale: Float,
    val tint: Color,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProvideModalComponentHost(
    modifier: Modifier = Modifier,
    state: ModalComponentHostState = rememberModalComponentHostState(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalModalComponentHostState provides state
    ) {
        val params = state.values.map { item ->
            val (_, backgroundBlur, backgroundTint, backgroundScaleRatio) = item.configs

            val animatedItemRatio = item.state.visibilityRatioState.value
            val blur = backgroundBlur * animatedItemRatio
            val scale = lerp(1f, backgroundScaleRatio, animatedItemRatio)
            val tint = backgroundTint.copy(backgroundTint.alpha * animatedItemRatio)
            Params(blur, scale, tint)
        }
        val contentBlur = params.map { it.blur.value }.sum().dp
        val contentScale = params
            .map { it.scale }
            .reduceOrNull { acc, num -> acc * num } ?: 1f

        @Composable
        fun Items(
            items: List<ModalComponentData>,
            index: Int,
        ) {
            if (items.isNotEmpty()) {
                val item = items.first()
                val (contentAlignment) = item.configs
                val isVisible = item.state.isVisible
                val param = params[index]
                val nextItems = items.drop(1)

                val nextItemsParams = params.drop(index + 1)
                val blurValues = nextItemsParams.map { it.blur.value }
                val scaleValues = nextItemsParams.map { it.scale }
                val blur = remember(blurValues) { blurValues.sum().dp }
                val scale = remember(scaleValues) {
                    scaleValues.reduceOrNull { acc, num -> acc * num } ?: 1f
                }

                LaunchedEffect(index) {
                    item.state.indexInStack = index
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(param.tint)
                ) {
                    // Current item content
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .blur(blur)
                            .scale(scale),
                        contentAlignment = contentAlignment
                    ) {
                        Spacer(
                            modifier = Modifier
                                .matchParentSize()
                                .then(
                                    if (isVisible) {
                                        Modifier.clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            if (item.dismissOnClickOutside) {
                                                item.onDismissRequest.invoke()
                                            }
                                        }
                                    } else Modifier
                                )
                        )

                        if (isVisible) {
                            BackHandler(item.dismissOnBackPress) {
                                item.onDismissRequest.invoke()
                            }

                            item.content()
                        }
                    }

                    // Next items contents
                    // Recursively call NestedBoxes for the rest of the items
                    Items(nextItems, index + 1)
                }
            }
        }

        Box(modifier = modifier) {
            Box(
                modifier = Modifier
                    .blur(contentBlur)
                    .scale(contentScale)
            ) {
                content()
            }
            Items(state.values, 0)
        }
    }
}

// Structure
//    val items = listOf("item 1", "item 2", "item 3", "item 4", "item 5")
//    content()
//    Box {
//        Box {
//            Box {
//                Box {
//                    Box {
//                        // item 1
//                    }
//                    // item 2
//                }
//                // item 3
//            }
//            // item 4
//        }
//        // item 5
//    }

class ModalComponentHostState {

    internal val values = mutableStateListOf<ModalComponentData>()

    fun add(data: ModalComponentData) {
        if (values.none { it.id == data.id }) {
            values.add(data)
        }
    }

    fun remove(data: ModalComponentData) {
        values.remove(data)
    }
}

@Composable
fun rememberModalComponentHostState() = remember { ModalComponentHostState() }

val LocalModalComponentHostState = compositionLocalOf { ModalComponentHostState() }