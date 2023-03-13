package bagus2x.tl.presentation.user.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ProfileTabs(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onClickTab: (Int) -> Unit,
    titles: List<Int>
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        titles.forEachIndexed { index, titleRes ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClickTab(index) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onBackground
            ) {
                Text(
                    text = stringResource(id = titleRes),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
