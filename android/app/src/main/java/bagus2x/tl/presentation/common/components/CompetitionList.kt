package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.domain.model.Competition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompetitionList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    competitions: List<Competition>,
    onItemClicked: (Long) -> Unit,
    onFavClicked: (Long) -> Unit
) {
    if (competitions.isEmpty()) {
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
        LazyColumn(
            state = lazyListState,
            modifier = modifier
        ) {
            items(
                items = competitions,
                key = { it.id }
            ) { competition ->
                CompetitionItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth()
                        .padding(16.dp),
                    poster = competition.poster,
                    title = competition.title,
                    organizer = competition.organizerName,
                    location = "${competition.city}, ${competition.country}",
                    deadline = competition.deadline,
                    minimumFee = competition.minimumFee,
                    maximumFee = competition.maximumFee,
                    favorite = competition.favorite,
                    onClick = {
                        onItemClicked(competition.id)
                    },
                    onFavClicked = {
                        onFavClicked(competition.id)
                    }
                )
                Divider()
            }
            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}
