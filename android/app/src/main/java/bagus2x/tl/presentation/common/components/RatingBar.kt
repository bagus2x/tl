package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.Tag

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    votes: Int,
    style: TextStyle = MaterialTheme.typography.body2,
    onChange: (Int, Float) -> Unit,
    enabled: Boolean = true,
    max: Int,
    size: Dp = 24.dp,
    color: Color = MaterialTheme.colors.secondary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingBar(
            rating = rating,
            onChange = onChange,
            enabled = enabled,
            max = max,
            size = size,
            color = color
        )
        Text(
            text = "($votes)",
            style = style
        )
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    onChange: (Int, Float) -> Unit,
    enabled: Boolean = true,
    max: Int,
    size: Dp = 24.dp,
    color: Color = MaterialTheme.colors.secondary
) {
    Row(modifier = modifier) {
        val value = rating * max
        val fullStar = value.toInt()
        var fractionStar = value - fullStar
        repeat(max) { index ->
            val fraction = if (index < fullStar) 1F else {
                val fraction = fractionStar
                fractionStar = 0F
                fraction
            }
            Star(
                modifier = Modifier.testTag("${Tag.BTN_STAR}-${index + 1}"),
                fraction = fraction,
                onClick = { onChange(index, (index + 1F) / max) },
                size = size,
                color = color,
                enabled = enabled
            )
        }
    }
}

@Composable
fun Star(
    modifier: Modifier = Modifier,
    fraction: Float,
    color: Color = Color.Yellow,
    onClick: () -> Unit,
    enabled: Boolean = true,
    size: Dp = 24.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(StarShape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .border(
                width = 1.dp,
                shape = StarShape,
                color = color
            )
            .drawBehind {
                val width = this.size.width
                val height = this.size.height
                drawRect(
                    color = color,
                    size = Size(
                        width = fraction * width,
                        height = height
                    )
                )
            }
    )
}

private val StarShape = GenericShape { size, _ ->
    moveTo(12.0f / 24 * size.width, 17.27f / 24 * size.height)
    lineTo(18.18f / 24 * size.width, 21.0f / 24 * size.height)
    relativeLineTo(-1.64f / 24 * size.width, -7.03f / 24 * size.height)
    lineTo(22.0f / 24 * size.width, 9.24f / 24 * size.height)
    relativeLineTo(-7.19f / 24 * size.width, -0.61f / 24 * size.height)
    lineTo(12.0f / 24 * size.width, 2.0f / 24 * size.height)
    lineTo(9.19f / 24 * size.width, 8.63f / 24 * size.height)
    lineTo(2.0f / 24 * size.width, 9.24f / 24 * size.height)
    relativeLineTo(5.46f / 24 * size.width, 4.73f / 24 * size.height)
    lineTo(5.82f / 24 * size.width, 21.0f / 24 * size.height)
    close()
}
