package bagus2x.tl.presentation.main.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bagus2x.tl.R
import bagus2x.tl.presentation.common.Tag

data class BotNavItem(
    @DrawableRes
    val outlinedRes: Int,
    @DrawableRes
    val filledRes: Int,
    @StringRes
    val titleRes: Int,
    val route: String,
    val tag: String
)

private val items = buildList {
    BotNavItem(
        outlinedRes = R.drawable.ic_home_outlined,
        filledRes = R.drawable.ic_home_filled,
        route = "announcement_list",
        titleRes = R.string.text_home,
        tag = Tag.BTN_HOME
    ).apply(::add)

    BotNavItem(
        outlinedRes = R.drawable.ic_trophy_outlined,
        filledRes = R.drawable.ic_trophy_filled,
        route = "competition_list",
        titleRes = R.string.text_competition,
        tag = Tag.BTN_COMPETITIONS
    ).apply(::add)

    BotNavItem(
        outlinedRes = R.drawable.ic_group_outlined,
        filledRes = R.drawable.ic_group_filled,
        route = "user_list",
        titleRes = R.string.text_users,
        tag = Tag.BTN_USERS
    ).apply(::add)

    BotNavItem(
        outlinedRes = R.drawable.ic_chat_outlined,
        filledRes = R.drawable.ic_chat_filled,
        route = "chat_list",
        titleRes = R.string.text_chat,
        tag = Tag.BTN_CHAT
    ).apply(::add)

    BotNavItem(
        outlinedRes = R.drawable.ic_person_outlined,
        filledRes = R.drawable.ic_person_filled,
        route = "profile",
        titleRes = R.string.text_profile,
        tag = Tag.BTN_PROFILE
    ).apply(::add)
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentHierarchy: () -> List<String?>,
    onClickItem: (String) -> Unit
) {
    val routes = remember { items.map { it.route } }
    val isVisible = currentHierarchy().any { it in routes }
    if (isVisible) {
        BottomNavigation(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
            elevation = 16.dp
        ) {
            items.forEach { (outlined, filled, titleRes, route, tag) ->
                val isSelected = currentHierarchy().any { it == route }
                val title = stringResource(id = titleRes)
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter =
                            if (isSelected)
                                painterResource(filled)
                            else
                                painterResource(outlined),
                            contentDescription = title
                        )
                    },
                    label = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.caption
                        )
                    },
                    selected = isSelected,
                    onClick = { onClickItem(route) },
                    modifier = Modifier.testTag(tag)
                )
            }
        }
    }
}
