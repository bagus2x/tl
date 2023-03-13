package bagus2x.tl.presentation.common.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.Utils
import bagus2x.tl.presentation.common.theme.TlRippleTheme
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.ZoneId

private val UnreadShape = RoundedCornerShape(8.dp)

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    photo: String?,
    name: String,
    description: String,
    updatedAt: LocalDateTime,
    unread: Int,
    onClick: () -> Unit
) = CompositionLocalProvider(LocalRippleTheme provides TlRippleTheme) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
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
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                    text = updatedAt.relativeTime,
                    style = MaterialTheme.typography.caption
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = description,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.caption,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (unread > 0) {
                    Surface(
                        shape = UnreadShape,
                        color = MaterialTheme.colors.primary,
                    ) {
                        Text(
                            text = "$unread",
                            modifier = Modifier
                                .widthIn(min = 20.dp)
                                .padding(2.dp),
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
