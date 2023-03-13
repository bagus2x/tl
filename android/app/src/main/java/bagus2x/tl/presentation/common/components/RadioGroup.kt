package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> RadioGroup(
    selected: T?,
    options: List<Pair<T, String>>,
    onChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    radioButtonColors: RadioButtonColors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((-16).dp)
    ) {
        options.forEach { option ->
            RadioButton(
                selected = selected == option.first,
                onCLick = { onChange(option.first) },
                text = { Text(text = option.second) },
                colors = radioButtonColors
            )
        }
    }
}
