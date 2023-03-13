package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.theme.TemanLombaTheme

@Composable
fun FilterOrderButton(
    modifier: Modifier = Modifier,
    onFilterClicked: () -> Unit,
    onSorterClicked: () -> Unit
) {
    var width by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    Row(
        modifier = modifier
            .widthIn(max = 180.dp)
            .scale(1F),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Button(
            onClick = onFilterClicked,
            modifier = Modifier.width(width),
            shape = RoundedCornerShape(
                topStart = 32.dp,
                bottomStart = 32.dp
            )
        ) {
            AutoResizedText(text = "Filters")
        }
        Button(
            onClick = onSorterClicked,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                width = with(localDensity) { coordinates.size.width.toDp() }
            },
            shape = RoundedCornerShape(
                topEnd = 32.dp,
                bottomEnd = 32.dp,
            )
        ) {
            AutoResizedText(text = "Sort By")
        }
    }
}

@Preview
@Composable
fun FilterSorterPreview() {
    TemanLombaTheme {
        FilterOrderButton(
            onFilterClicked = { },
            onSorterClicked = { }
        )
    }
}
