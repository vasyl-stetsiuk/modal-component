package com.stetsiuk.modal_component

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

internal const val DEFAULT_ANIM_DURATION = 300

class ModalComponentState(
    @FloatRange(0.0, 1.0) initialVisibilityRatio: Float = 0f,
) {
    internal val visibilityRatioState = Animatable(initialVisibilityRatio)
    val visibilityRatio by visibilityRatioState.asState()

    val isVisible get() = visibilityRatio > 0
    var indexInStack by mutableStateOf<Int?>(null)
        internal set

    suspend fun snapTo(
        @FloatRange(0.0, 1.0) value: Float
    ) {
        visibilityRatioState.snapTo(value)
    }

    suspend fun show(
        animationSpec: AnimationSpec<Float> = defaultAnimationSpec()
    ) {
        visibilityRatioState.animateTo(1f, animationSpec)
    }

    suspend fun hide(
        animationSpec: AnimationSpec<Float> = defaultAnimationSpec()
    ) {
        visibilityRatioState.animateTo(0f, animationSpec)
    }

    companion object {
        val Saver: Saver<ModalComponentState, *> = Saver(
            save = {
                listOf(it.visibilityRatio, it.indexInStack)
            },
            restore = {
                ModalComponentState(it[0] as Float).apply {
                    indexInStack = it[1] as Int
                }
            }
        )
    }
}

@Composable
fun rememberModalComponentState(
    @FloatRange(0.0, 1.0) initialVisibilityRatio: Float = 0f,
) = rememberSaveable(saver = ModalComponentState.Saver) {
    ModalComponentState(initialVisibilityRatio = initialVisibilityRatio)
}

@Composable
fun rememberModalComponentState(
    initialIsVisible: Boolean = false,
) = rememberModalComponentState(
    initialVisibilityRatio = if (initialIsVisible) 1f else 0f
)

private fun defaultAnimationSpec(
    duration: Int = DEFAULT_ANIM_DURATION,
): AnimationSpec<Float> = tween(durationMillis = duration)