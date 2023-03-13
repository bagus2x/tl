package bagus2x.tl.presentation.common.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.components.RatingBar
import bagus2x.tl.presentation.common.components.UserProfile
import java.time.LocalDateTime

@Composable
fun TestimonyItem(
    modifier: Modifier = Modifier,
    photo: String?,
    name: String,
    department: String?,
    university: String?,
    batch: Int?,
    createdAt: LocalDateTime,
    @FloatRange(from = 0.0, to = 1.0)
    rating: Float,
    description: String
) {
    Column(modifier = modifier) {
        UserProfile(
            modifier = Modifier.fillMaxWidth(),
            photo = photo,
            name = name,
            department = department,
            university = university,
            batch = batch,
            timestamp = createdAt
        )
        Spacer(modifier = Modifier.height(8.dp))
        RatingBar(
            rating = rating,
            onChange = { _, _ -> },
            max = 5
        )
        Text(
            text = description,
            style = MaterialTheme.typography.body2,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
