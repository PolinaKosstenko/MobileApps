package com.example.harrypotterapi.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.harrypotterapi.model.Spell
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UiState
import com.example.harrypotterapi.ui.widget.ErrorView
import com.example.harrypotterapi.ui.widget.LoadingView
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sqrt

@Composable
fun LearningScreen(
    viewModel: HarryPotterAPIViewModel,
    spellId: Int,
    brushColor: Color = Color.Red,
    brushWidth: Float = 90f,
    onDrawingComplete: (String, Float) -> Unit
) {
    LaunchedEffect(spellId) {
        viewModel.selectSpell(spellId)
    }

    val state by viewModel.selectedSpellUiState.collectAsStateWithLifecycle()

    when (val uiState = state) {
        is UiState.Loading -> LoadingView()
        is UiState.Error -> ErrorView(
            "Ошибка загрузки заклинания! ${uiState.message}",
            viewModel::onRetry
        )

        is UiState.Success -> LearningSpellView(
            uiState.data,
            brushColor,
            brushWidth,
            onDrawingComplete
        )
    }
}

@Composable
fun LearningSpellView(
    spell: Spell,
    brushColor: Color = Color.Red,
    brushWidth: Float = 90f,
    onDrawingComplete: (String, Float) -> Unit
) {
    val strokes = remember { mutableStateListOf<StrokeData>() }

    var currentStroke by remember { mutableStateOf<StrokeData>(StrokeData()) }

    val resId = LocalContext.current.resources.getIdentifier(
        spell.image,
        "drawable",
        LocalContext.current.packageName
    )

    val spellBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, resId)

    var timeLeft by remember { mutableIntStateOf(15) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(isTimerRunning) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }

        val deviation = calculateDeviation(
            strokes = strokes,
            spellBitmap = spellBitmap,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height
        )

        onDrawingComplete(spell.name, deviation)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                }
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
                                    currentStroke.let {
                                        strokes.add(it)
                                    }
                                    currentStroke = currentStroke.copy(path = Path())
                                }
                            }
                        }
                    }
                }
        ) {
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

            currentStroke.let { stroke ->
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Учим заклинание ${spell.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Проведите палочкой по силуэту заклинания как можно точнее",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = "Осталось ${timeLeft} секунд",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (timeLeft <= 5) Color.Red else Color.White
                )
            }
        }
    }
}

data class StrokeData(
    val path: Path = Path(),
    val color: Color = Color.Black,
    val strokeWidth: Float = 8f
)

fun calculateDeviation(
    strokes: List<StrokeData>,
    spellBitmap: Bitmap,
    canvasWidth: Float,
    canvasHeight: Float
): Float {
    if (strokes.isEmpty()) return 100f

    val scaleX = spellBitmap.width.toFloat() / canvasWidth
    val scaleY = spellBitmap.height.toFloat() / canvasHeight

    val pathMeasure = PathMeasure()
    var totalDeviation = 0f
    var totalSamples = 0
    val step = 5f

    for (stroke in strokes) {
        pathMeasure.setPath(stroke.path, false)
        val length = pathMeasure.length

        if (length == 0f) continue

        var d = 0f
        while (d < length) {
            val pos = pathMeasure.getPosition(d)

            val bitmapX = pos.x * scaleX
            val bitmapY = pos.y * scaleY

            val dist = distanceToNearestBlackPixel(
                Offset(bitmapX, bitmapY),
                spellBitmap
            )

            totalDeviation += dist
            totalSamples++

            d += step
        }
    }

    if (totalSamples == 0) return 100f

    val avgDeviationBitmapPx = totalDeviation / totalSamples

    val maxDeviation = 100f
    return (avgDeviationBitmapPx / maxDeviation).coerceIn(0f, 1f) * 100f
}

fun distanceToNearestBlackPixel(
    point: Offset,
    bitmap: Bitmap,
    maxRadius: Int = 30
): Float {
    val x = point.x.toInt()
    val y = point.y.toInt()

    if (x in 0 until bitmap.width && y in 0 until bitmap.height) {
        if (isBlackPixel(bitmap, x, y)) return 0f
    }

    for (radius in 1..maxRadius) {
        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                if (abs(dx) != radius && abs(dy) != radius) continue

                val checkX = x + dx
                val checkY = y + dy

                if (checkX < 0 || checkX >= bitmap.width || checkY < 0 || checkY >= bitmap.height) continue

                if (isBlackPixel(bitmap, checkX, checkY)) {
                    return sqrt((dx * dx + dy * dy).toFloat())
                }
            }
        }
    }

    return maxRadius.toFloat()
}

fun isBlackPixel(bitmap: Bitmap, x: Int, y: Int): Boolean {
    val pixel = bitmap.getPixel(x, y)
    return Color(pixel).let { color ->
        color.red < 0.3f && color.green < 0.3f && color.blue < 0.3f
    }
}
