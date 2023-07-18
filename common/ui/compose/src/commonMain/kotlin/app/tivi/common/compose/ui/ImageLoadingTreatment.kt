// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.tivi.common.compose.ui

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ColorMatrix
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun animateImageLoadingColorMatrixAsState(
    enabled: Boolean = true,
    duration: Duration = 1.seconds,
    onStarted: (() -> Unit)? = null,
    onFinished: (() -> Unit)? = null,
): State<ColorMatrix> = produceState(
    initialValue = ColorMatrix().withAlpha(0f),
    enabled,
) {
    if (!enabled) {
        value = ColorMatrix()
        return@produceState
    }

    val durationMs = duration.inWholeMilliseconds.toInt()

    try {
        coroutineScope {
            onStarted?.invoke()

            launch {
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    initialVelocity = 0f,
                    animationSpec = tween(durationMs),
                ) { sat, _ ->
                    value = value.copy().withSaturation(sat)
                }
            }

            launch {
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    initialVelocity = 0f,
                    animationSpec = tween(durationMs / 2),
                ) { alpha, _ ->
                    value = value.copy().withAlpha(alpha)
                }
            }

            launch {
                animate(
                    initialValue = 0.8f,
                    targetValue = 1f,
                    initialVelocity = 0f,
                    animationSpec = tween(durationMs * 3 / 4),
                ) { brightness, _ ->
                    value = value.copy().withBrightness(brightness)
                }
            }
        }
    } finally {
        value = ColorMatrix()
        onFinished?.invoke()
    }
}

fun ColorMatrix.copy(): ColorMatrix = ColorMatrix(values = floatArrayOf(*values))

fun ColorMatrix.withSaturation(sat: Float): ColorMatrix {
    val invSat = 1 - sat
    val R = 0.213f * invSat
    val G = 0.715f * invSat
    val B = 0.072f * invSat
    this[0, 0] = R + sat
    this[0, 1] = G
    this[0, 2] = B
    this[1, 0] = R
    this[1, 1] = G + sat
    this[1, 2] = B
    this[2, 0] = R
    this[2, 1] = G
    this[2, 2] = B + sat

    return this
}

fun ColorMatrix.withBrightness(brightness: Float): ColorMatrix {
    // We subtract to make the picture look darker, it will automatically clamp
    val darkening = (1 - brightness) * MAX_DARKEN_PERCENTAGE * 255
    this[0, 4] = -darkening
    this[1, 4] = -darkening
    this[2, 4] = -darkening

    return this
}

// This means that we darken the image by 20%
private const val MAX_DARKEN_PERCENTAGE = 0.2f

fun ColorMatrix.withAlpha(alpha: Float): ColorMatrix {
    this[3, 3] = alpha
    return this
}
