package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    description: String,
    createdAt: LocalDateTime,
    backgroundColor: Color = MaterialTheme.colors.onBackground.copy(0.1F),
    borderColor: Color = MaterialTheme.colors.onBackground.copy(0.4F),
    fraction: Float = 0.5F,
    radius: Dp = 8.dp,
    triangleHeight: Dp = 16.dp,
    triangleBottom: Dp = 20.dp,
    arrangementEnd: Boolean
) {
    val arrangement = if (arrangementEnd) Arrangement.Start else Arrangement.End
    Row(
        horizontalArrangement = arrangement,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .then(
                    Modifier.drawTriangle(
                        backgroundColor,
                        borderColor,
                        radius,
                        triangleHeight,
                        triangleBottom,
                        arrangementEnd
                    )
                )
        ) {
            Text(
                text = description,
                modifier = Modifier.padding(start = 8.dp, top = 24.dp, end = 16.dp),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = createdAt.hoursMinutes,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

private val LocalDateTime.hoursMinutes: String
    @Composable
    get() {
        return rememberSaveable {
            val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
            format(formatter)
        }
    }


private fun Modifier.drawTriangle(
    backgroundColor: Color,
    borderColor: Color,
    radius: Dp,
    triangleHeight: Dp,
    triangleBottom: Dp,
    arrangementEnd: Boolean
): Modifier = composed {
    val density = LocalDensity.current
    val radiusPx = with(density) { radius.toPx() }
    val triangleHeightPx = with(density) { triangleHeight.toPx() }
    val triangleBottomPx = with(density) { triangleBottom.toPx() }
    drawBehind {
        val path = Path().apply {
            if (arrangementEnd) {
                moveTo(0F, triangleHeightPx)
                lineTo(size.width - triangleBottomPx, triangleHeightPx)
                lineTo(size.width, 0F)
                lineTo(size.width, size.height)
                lineTo(0F, size.height)
                close()
                return@apply
            }
            moveTo(0F, 0F)
            lineTo(triangleBottomPx, triangleHeightPx)
            lineTo(size.width, triangleHeightPx)
            lineTo(size.width, size.height)
            lineTo(0F, size.height)
            close()
        }
        drawPath(
            path = path,
            color = borderColor,
            style = Stroke(
                pathEffect = PathEffect.cornerPathEffect(radiusPx)
            )
        )
        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(path),
                paint = Paint().apply {
                    color = backgroundColor
                    pathEffect = PathEffect.cornerPathEffect(radiusPx)
                }
            )
        }
    }
}

