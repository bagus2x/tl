package bagus2x.tl.presentation.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Competition
import bagus2x.tl.domain.model.User
import bagus2x.tl.presentation.common.components.CompetitionList
import bagus2x.tl.presentation.common.components.UserList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val competitions by viewModel.competitions.collectAsState()
    FavoritesScreen(
        navigateUp = navController::navigateUp,
        users = users,
        navigateToUserDetail = { userId ->
            navController.navigate("user_detail/$userId")
        },
        favoriteUser = viewModel::favoriteUser,
        competitions = competitions,
        navigateToCompetitionDetail = { competitionId ->
            navController.navigate("competition_detail/$competitionId")
        },
        favoriteCompetition = viewModel::favoriteCompetition
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FavoritesScreen(
    navigateUp: () -> Unit,
    users: List<User>,
    navigateToUserDetail: (Long) -> Unit,
    favoriteUser: (Long) -> Unit,
    competitions: List<Competition>,
    navigateToCompetitionDetail: (Long) -> Unit,
    favoriteCompetition: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.text_like))
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            val titles = remember {
                listOf(
                    context.getString(R.string.text_users),
                    context.getString(R.string.text_competition),
                )
            }
            val pagerState = rememberPagerState()
            val scope = rememberCoroutineScope()
            LikesTabs(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                onClickTab = {
                    scope.launch { pagerState.animateScrollToPage(it) }
                },
                titles = titles,
            )
            HorizontalPager(
                count = titles.size,
                modifier = Modifier.weight(1F),
                state = pagerState
            ) { index ->
                if (index == 0) {
                    UserList(
                        users = users,
                        onItemClicked = navigateToUserDetail,
                        onFavClicked = favoriteUser,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (index == 1) {
                    CompetitionList(
                        competitions = competitions,
                        onItemClicked = navigateToCompetitionDetail,
                        onFavClicked = favoriteCompetition,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun LikesTabs(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onClickTab: (Int) -> Unit,
    titles: List<String>
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClickTab(index) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onBackground
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
