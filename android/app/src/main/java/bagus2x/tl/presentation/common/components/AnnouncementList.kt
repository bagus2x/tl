package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.domain.model.Announcement
import bagus2x.tl.presentation.common.LocalAuthUser
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnnouncementList(
    modifier: Modifier = Modifier,
    announcements: List<Announcement>,
    onItemClicked: ((Long) -> Unit)? = null,
) {
    if (announcements.isEmpty()) {
        Box(modifier) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.not_found))
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                iterations = LottieConstants.IterateForever,
            )
        }
    } else {
        val authUser = LocalAuthUser.current
        LazyColumn(modifier) {
            items(
                items = announcements,
                key = { it.id }
            ) { announcement ->
                AnnouncementItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth()
                        .padding(16.dp),
                    photo = announcement.author.photo,
                    name = announcement.author.name,
                    department = announcement.author.department,
                    university = announcement.author.university,
                    batch = announcement.author.batch,
                    timestamp = announcement.createdAt,
                    description = announcement.description,
                    file = announcement.file,
                    onClick = {
                        if (onItemClicked != null && announcement.author.id != authUser?.userId) {
                            onItemClicked(announcement.author.id)
                        }
                    }
                )
                Divider()
            }
        }
    }
}
