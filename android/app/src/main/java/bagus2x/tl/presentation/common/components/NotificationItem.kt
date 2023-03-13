package bagus2x.tl.presentation.common.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.Utils
import bagus2x.tl.presentation.common.theme.TlRippleTheme
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    photo: String?,
    title: String,
    subtitle1: String?,
    subtitle2: String?,
    text: String,
    createdAt: LocalDateTime,
    onClick: () -> Unit
) = CompositionLocalProvider(LocalRippleTheme provides TlRippleTheme) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = photo ?: Utils.profile(title),
            contentDescription = title,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = createdAt.relativeTime,
                    style = MaterialTheme.typography.caption
                )
            }
            if (!subtitle1.isNullOrBlank()) {
                Text(
                    text = subtitle1,
                    style = MaterialTheme.typography.caption
                )
            }
            if (!subtitle2.isNullOrBlank()) {
                Text(
                    text = subtitle2,
                    style = MaterialTheme.typography.caption
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.body2
            )
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
