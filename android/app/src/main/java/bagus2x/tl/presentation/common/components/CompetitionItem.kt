package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Domain
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.theme.TlRippleTheme
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CompetitionItem(
    modifier: Modifier = Modifier,
    poster: String?,
    title: String,
    organizer: String,
    location: String,
    deadline: LocalDateTime,
    minimumFee: Long,
    maximumFee: Long,
    favorite: Boolean,
    onClick: () -> Unit,
    onFavClicked: () -> Unit
) = CompositionLocalProvider(LocalRippleTheme provides TlRippleTheme) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = poster,
                contentDescription = title,
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            FavoriteButton(
                favorite = favorite,
                onClick = onFavClicked,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                iconSize = 16.dp
            )
        }
        Column(
            modifier = Modifier
                .weight(1F)
                .align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            val iconText = @Composable { icon: ImageVector, text: String ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            iconText(Icons.Outlined.Domain, organizer)
            iconText(Icons.Outlined.LocationOn, location)
            iconText(Icons.Outlined.Event, deadline.formattedDate)
            Text(
                text = formatRupiah(minimumFee, maximumFee),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private val locale = Locale("id", "ID")
private val numberFormat = NumberFormat.getCurrencyInstance(locale)

@Composable
private fun formatRupiah(minimumFee: Long, maximumFee: Long): String {
    return rememberSaveable {
        listOf(minimumFee, maximumFee)
            .sorted()
            .filter { it != 0L }
            .joinToString(" - ") { numberFormat.format(it) }
    }
}

private val LocalDateTime.formattedDate: String
    @Composable
    get() {
        return rememberSaveable {
            val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())
            format(formatter)
        }
    }
