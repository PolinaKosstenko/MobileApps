package com.example.harrypotterapi.ui.widget


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun DrawingCanvas(
    brushColor: Color = Color.Black,
    brushWidth: Float = 8f,
    imageBitmap: ImageBitmap? = null,  // Pass image to draw underneath
    imageResId: Int? = null            // Or pass resource ID
) {
    // Store completed strokes
    val strokes = remember { mutableStateListOf<StrokeData>() }

    // Current stroke being drawn
    var currentStroke by remember { mutableStateOf<StrokeData>(StrokeData()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Draw image underneath (layer 1)
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else if (imageResId != null) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(brushColor, brushWidth) {
                var currentPath = Path()

                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        val position = event.changes.firstOrNull()?.position

                        when (event.type) {
                            PointerEventType.Press -> {
                                position?.let {
                                    currentPath = Path().apply { moveTo(it.x, it.y) }
                                    currentStroke = StrokeData(
                                        path = currentPath,
                                        color = brushColor,
                                        strokeWidth = brushWidth
                                    )
                                }
                            }

                            PointerEventType.Move -> {
                                if (event.changes.first().pressed && position != null) {
                                    currentPath.lineTo(position.x, position.y)
                                    currentStroke = currentStroke.copy(
                                        path = currentPath.copy(),
                                        color = brushColor,
                                        strokeWidth = brushWidth
                                    )
                                }
                            }

                            PointerEventType.Release -> {
                                currentStroke?.let {
                                    strokes.add(it)
                                }
                                currentStroke = currentStroke.copy(path = Path())
                            }
                        }
                    }
                }
            }
    ) {
        // Draw all completed strokes
        strokes.forEach { stroke ->
            drawPath(
                path = stroke.path,
                color = stroke.color,
                style = Stroke(
                    width = stroke.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // Draw current stroke (updates in real-time)
        currentStroke?.let { stroke ->
            drawPath(
                path = stroke.path,
                color = stroke.color,
                style = Stroke(
                    width = stroke.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

data class StrokeData(
    val path: Path = Path(),
    val color: Color = Color.Black,
    val strokeWidth: Float = 8f
)
