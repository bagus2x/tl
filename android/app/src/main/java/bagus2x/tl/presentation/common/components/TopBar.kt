package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalUnreadNotifications
import bagus2x.tl.presentation.common.Tag

typealias OnClick = () -> Unit

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    titleText: String,
    onBackClicked: (() -> Unit)? = null,
    onSearchClicked: (() -> Unit)? = null,
    onFavoritesClicked: (() -> Unit)? = null,
    onNotificationClicked: (() -> Unit)? = null,
    onShareClicked: (() -> Unit)? = null,
    onAddClicked: (() -> Unit)? = null,
    buttonText: String? = null,
    onButtonClicked: (() -> Unit)? = null,
    buttonEnabled: Boolean = true,
    onEditClicked: (() -> Unit)? = null,
    onSignOutClicked: (() -> Unit)? = null,
) {
    val iconButton = @Composable { icon: ImageVector, onClick: OnClick, contentDescription: String? ->
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = titleText)
        },
        navigationIcon = when {
            onBackClicked != null -> {
                { iconButton(Icons.Rounded.ArrowBack, onBackClicked, null) }
            }
            else -> null
        },
        actions = {
            onSearchClicked?.let {
                iconButton(Icons.Rounded.Search, it, null)
            }
            onFavoritesClicked?.let {
                iconButton(Icons.Rounded.Favorite, it, null)
            }
            val unreadNotifications = LocalUnreadNotifications.current
            if (onNotificationClicked != null && unreadNotifications != 0) {
                IconButton(onClick = onNotificationClicked) {
                    BadgedBox(
                        badge = {
                            Badge(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.primary,
                                modifier = Modifier.border(
                                    shape = CircleShape,
                                    width = 1.dp,
                                    color = MaterialTheme.colors.primary
                                )
                            ) { Text(text = "$unreadNotifications") }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            } else if (onNotificationClicked != null) {
                iconButton(Icons.Rounded.Notifications, onNotificationClicked, stringResource(R.string.text_notification))
            }
            onShareClicked?.let {
                iconButton(Icons.Rounded.Share, it, null)
            }
            onAddClicked?.let {
                iconButton(Icons.Rounded.Add, it, null)
            }
            if (!buttonText.isNullOrEmpty() && onButtonClicked != null) {
                Button(
                    onClick = onButtonClicked,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .testTag(Tag.BTN_ACTION),
                    enabled = buttonEnabled
                ) {
                    Text(text = buttonText)
                }
            }
            if (onEditClicked != null && onSignOutClicked != null) {
                var isDialogOpened by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { isDialogOpened = true }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(R.string.text_see_more),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                Dialog(
                    visible = isDialogOpened,
                    onDismissRequest = { isDialogOpened = false }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextButton(
                            onClick = onEditClicked,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = stringResource(R.string.text_edit_profile))
                        }
                        TextButton(
                            onClick = onSignOutClicked,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = stringResource(R.string.text_signout))
                        }
                    }
                }
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary
    )
}
