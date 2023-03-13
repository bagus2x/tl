package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.tl.R

@Composable
fun CompetitionFilterDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    options: Map<String, Map<String, Boolean>> = emptyMap(),
    onChange: (Map<String, Map<String, Boolean>>) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val filter = remember(options, visible) { mutableStateMapOf(*options.toList().toTypedArray()) }
    Dialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            for ((categoryTitle, category) in filter) {
                Text(
                    text = when (categoryTitle) {
                        "category" -> stringResource(R.string.text_competition_level)
                        "organizer" -> stringResource(R.string.text_organizer)
                        "theme" -> stringResource(R.string.text_theme)
                        else -> categoryTitle
                    },
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    for ((label, selected) in category) {
                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = selected,
                                    onCheckedChange = { checked ->
                                        val labels = filter[categoryTitle]!!
                                        filter[categoryTitle] = labels.mapValues {
                                            if (it.key == label) checked
                                            else it.value
                                        }
                                    }
                                )
                                Text(
                                    text = when (label) {
                                        "national" -> stringResource(R.string.text_national)
                                        "international" -> stringResource(R.string.text_international)
                                        "government" -> stringResource(R.string.text_government)
                                        "company" -> stringResource(R.string.text_company)
                                        "university" -> stringResource(R.string.text_university)
                                        else -> label
                                    },
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
            Button(
                onClick = {
                    onChange(filter.toMap())
                    onDismissRequest()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(R.string.text_save))
            }
        }
    }
}
