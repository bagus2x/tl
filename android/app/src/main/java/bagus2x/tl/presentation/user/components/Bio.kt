package bagus2x.tl.presentation.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Male
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bagus2x.tl.R

@Composable
fun Bio(
    modifier: Modifier = Modifier,
    gender: String?,
    age: Int?,
    city: String?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.text_bio),
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        if (!gender.isNullOrBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = gender.genderIcon,
                    contentDescription = null
                )
                Text(
                    text = when (gender) {
                        "M" -> stringResource(R.string.text_male)
                        "F" -> stringResource(R.string.text_female)
                        else -> "-"
                    },
                    style = MaterialTheme.typography.body2
                )
            }
        }
        if (age != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Circle,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.text_years_old, age),
                    style = MaterialTheme.typography.body2
                )
            }
        }
        if (!city.isNullOrBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null
                )
                Text(
                    text = city,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

private val String.genderIcon: ImageVector
    get() = if (lowercase() == "m") Icons.Rounded.Male else Icons.Rounded.Female
