package bagus2x.tl.presentation.user.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bagus2x.tl.R
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.Utils
import bagus2x.tl.presentation.common.components.noRippleClickable
import coil.compose.AsyncImage

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    photo: String?,
    name: String,
    bio: String?,
    friends: Int,
    likes: Int,
    friendshipStatus: String? = null,
    onInvitationClicked: (() -> Unit)? = null,
    onReqFriendshipClicked: (() -> Unit)? = null,
    onTestimonyClicked: (() -> Unit)? = null,
    onFriendsClicked: () -> Unit,
    onLikesClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = photo ?: Utils.profile(name),
                contentDescription = name,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.noRippleClickable(onFriendsClicked),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$friends",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Friends",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier.noRippleClickable(onLikesClicked),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$likes",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Likes",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            if (!bio.isNullOrEmpty()) {
                Text(
                    text = bio,
                    style = MaterialTheme.typography.body2,
                    maxLines = 5
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (onInvitationClicked != null) {
                Button(
                    onClick = onInvitationClicked,
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.BTN_INVITE),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_invite),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.button.copy(fontSize = 11.sp)
                    )
                }
            }
            if (onReqFriendshipClicked != null) {
                Button(
                    onClick = onReqFriendshipClicked,
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.BTN_REQ_FRIEND),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = when (friendshipStatus) {
                            "friendship_requested" -> stringResource(R.string.text_requested)
                            "friendship_accepted" -> stringResource(R.string.text_friend)
                            else -> stringResource(R.string.text_request_friendship)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.button.copy(fontSize = 11.sp)
                    )
                }
            }
            if (onTestimonyClicked != null) {
                Button(
                    onClick = onTestimonyClicked,
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.BTN_ADD_TESTIMONY),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_testimony),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.button.copy(fontSize = 11.sp)
                    )
                }
            }
        }
    }
}
