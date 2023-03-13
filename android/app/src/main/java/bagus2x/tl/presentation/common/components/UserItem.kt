package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.Utils
import bagus2x.tl.presentation.common.theme.TlRippleTheme
import coil.compose.AsyncImage

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    photo: String?,
    name: String,
    department: String?,
    university: String?,
    batch: Int?,
    rating: Float,
    votes: Int,
    favorite: Boolean,
    onItemClicked: () -> Unit,
    onFavClicked: () -> Unit
) = CompositionLocalProvider(LocalRippleTheme provides TlRippleTheme) {
    Row(
        modifier = Modifier
            .clickable(onClick = onItemClicked)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box {
            AsyncImage(
                model = photo ?: Utils.profile(name),
                contentDescription = name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            FavoriteButton(
                favorite = favorite,
                onClick = onFavClicked,
                modifier = Modifier.align(Alignment.BottomEnd),
                iconSize = 16.dp
            )
        }
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.noRippleClickable(onClick = onItemClicked)
            )
            if (department != null) {
                Text(
                    text = department,
                    style = MaterialTheme.typography.caption
                )
            }
            if (university != null) {
                Text(
                    text = university,
                    style = MaterialTheme.typography.caption
                )
            }
            if (batch != null) {
                Text(
                    text = "$batch",
                    style = MaterialTheme.typography.caption
                )
            }
            RatingBar(
                rating = rating,
                onChange = { _, _ -> },
                enabled = false,
                max = 5,
                votes = votes
            )
        }
    }
}
