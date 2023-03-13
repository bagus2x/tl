package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RadioButton(
    selected: Boolean,
    onCLick: () -> Unit,
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    colors: RadioButtonColors = RadioButtonDefaults.colors()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onCLick),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onCLick,
            colors = colors
        )
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.body2) {
            text()
        }
    }
}
