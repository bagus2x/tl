package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.presentation.common.theme.TemanLombaTheme
import java.time.LocalDateTime

@Composable
fun AnnouncementItem(
    modifier: Modifier = Modifier,
    photo: String?,
    name: String,
    department: String?,
    university: String?,
    batch: Int?,
    timestamp: LocalDateTime,
    description: String,
    file: String?,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UserProfile(
            modifier = Modifier.fillMaxWidth(),
            photo = photo,
            name = name,
            department = department,
            university = university,
            batch = batch,
            timestamp = timestamp
        )
        ExpandableText(
            text = description,
            collapsedMaxLine = 6,
            style = MaterialTheme.typography.body2,
        )
        if (file != null) {
            AsyncImage(
                model = file,
                contentDescription = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1F)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colors.onBackground.copy(0.5F)),
                contentScale = ContentScale.FillWidth
            )
        }
        if (onClick != null) {
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.text_apply))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnouncementPreview() {
    TemanLombaTheme {
        AnnouncementItem(
            modifier = Modifier.fillMaxWidth(),
            photo = "https://wac-cdn.atlassian.com/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=573",
            name = "Username",
            department = "Department name",
            university = "University name",
            batch = 2020,
            timestamp = LocalDateTime.now().minusDays(3),
            description = LoremIpsum(200).values.joinToString(" "),
            file = "https://tbindonesia.or.id/wp-content/uploads/2021/06/lomba_poster_hari_anak-819x1024.png"
        )
    }
}
