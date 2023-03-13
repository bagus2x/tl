package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.domain.model.User
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserList(
    modifier: Modifier = Modifier,
    users: List<User>,
    onItemClicked: (Long) -> Unit,
    onFavClicked: (Long) -> Unit
) {
    if (users.isEmpty()) {
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
                items = users,
                key = { it.id }
            ) { user ->
                var favorite by rememberSaveable(user.favorite) { mutableStateOf(user.favorite) }
                UserItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(16.dp),
                    photo = user.photo,
                    name = user.name,
                    department = user.department,
                    university = user.university,
                    batch = user.batch,
                    rating = user.rating,
                    votes = user.votes,
                    favorite = user.favorite,
                    onItemClicked = { onItemClicked(user.id) },
                    onFavClicked = {
                        onFavClicked(user.id)
                        favorite = !favorite
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
