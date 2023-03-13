package bagus2x.tl.presentation.common.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.Utils
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun UserProfile(
    modifier: Modifier = Modifier,
    photo: String?,
    name: String,
    department: String?,
    university: String?,
    batch: Int?,
    timestamp: LocalDateTime
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = photo ?: Utils.profile(name),
            contentDescription = name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1F)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = timestamp.relativeTime,
                    style = MaterialTheme.typography.caption
                )
            }
            if (department != null) {
                Text(
                    text = department,
                    style = MaterialTheme.typography.caption
                )
            }
            if (university != null) {
                Text(
                    text = if (batch != null) "$university $batch" else university,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

private val LocalDateTime.relativeTime: String
    @Composable
    get() {
        return rememberSaveable {
            val time = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val now = System.currentTimeMillis()
            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
        }
    }
