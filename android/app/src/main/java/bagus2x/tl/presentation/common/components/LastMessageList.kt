package bagus2x.tl.presentation.common.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.domain.model.Message
import bagus2x.tl.presentation.common.LocalAuthUser
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LastMessageList(
    lastMessages: List<Message>,
    onItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (lastMessages.isEmpty()) {
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
        val auth = LocalAuthUser.current
        LazyColumn(modifier = modifier) {
            items(
                items = lastMessages,
                key = { it.id }
            ) { message ->
                val user =
                    if (auth?.userId == message.sender.id) message.receiver
                    else message.sender
                ChatItem(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    photo = user.photo,
                    name = user.name,
                    description =
                    if (message.sender.id == auth?.userId)
                        stringResource(R.string.text_message_you, message.description)
                    else
                        message.description,
                    updatedAt = message.createdAt,
                    unread = message.totalUnread,
                    onClick = { onItemClicked(user.id) }
                )
                Divider()
            }
        }
    }
}
