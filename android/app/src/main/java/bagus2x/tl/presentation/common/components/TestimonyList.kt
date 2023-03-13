package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.layout.Box
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
import bagus2x.tl.domain.model.Testimony
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun TestimonyList(
    modifier: Modifier = Modifier,
    testimonies: List<Testimony>
) {
    if (testimonies.isEmpty()) {
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
        LazyColumn(modifier = modifier) {
            items(
                items = testimonies,
                key = { it.id }
            ) { testimony ->
                TestimonyItem(
                    modifier = Modifier.padding(16.dp),
                    photo = testimony.author.photo,
                    name = testimony.author.name,
                    department = testimony.author.department,
                    university = testimony.author.university,
                    batch = testimony.author.batch,
                    createdAt = testimony.createdAt,
                    rating = testimony.rating,
                    description = testimony.description
                )
                Divider()
            }
        }
    }
}
