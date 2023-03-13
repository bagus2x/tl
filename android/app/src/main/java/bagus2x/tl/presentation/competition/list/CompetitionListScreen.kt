package bagus2x.tl.presentation.competition.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Competition
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.*
import bagus2x.tl.presentation.common.components.Scaffold

@Composable
fun CompetitionListScreen(
    navController: NavController,
    viewModel: CompetitionListViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val snackbarState = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            snackbarState(snackbar)
        }
    }

    val competitions by viewModel.competitions.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val order by viewModel.order.collectAsState()
    CompetitionListScreen(
        competitions = competitions,
        filter = filter,
        setFilter = viewModel::setFilter,
        order = order,
        setOrder = viewModel::setOrder,
        navigateToSearch = {
            navController.navigate("search/competitions")
        },
        navigateToFavorites = {
            navController.navigate("favorites")
        },
        navigateToNotifications = {
            navController.navigate("notifications")
        },
        navigateToCompetitionForm = {
            navController.navigate("competition_form")
        },
        navigateToCompetitionDetail = { competitionId ->
            navController.navigate("competition_detail/$competitionId")
        },
        favoriteCompetition = viewModel::toggleFavorite
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CompetitionListScreen(
    competitions: List<Competition>,
    filter: Map<String, Map<String, Boolean>>,
    setFilter: (Map<String, Map<String, Boolean>>) -> Unit,
    order: String,
    setOrder: (String) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToCompetitionForm: () -> Unit,
    navigateToCompetitionDetail: (Long) -> Unit,
    favoriteCompetition: (Long) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val isOrderFilterBtnVisible = isOrderFilterBtnVisible(order, filter)
    Scaffold(
        modifier = Modifier.testTag(Tag.COMPETITION_LIST_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_competition),
                onSearchClicked = navigateToSearch,
                onFavoritesClicked = navigateToFavorites,
                onNotificationClicked = navigateToNotifications
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisible,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = navigateToCompetitionForm,
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier.testTag(Tag.BTN_ADD)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        CompetitionList(
            lazyListState = lazyListState,
            competitions = competitions,
            onFavClicked = favoriteCompetition,
            onItemClicked = navigateToCompetitionDetail,
            modifier = Modifier.fillMaxSize(),
        )
        var filterDialogVisible by remember { mutableStateOf(false) }
        var sorterDialogVisible by remember { mutableStateOf(false) }
        AnimatedVisibility(
            visible = !fabVisible || isOrderFilterBtnVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            FilterOrderButton(
                modifier = Modifier.padding(16.dp),
                onFilterClicked = { filterDialogVisible = true },
                onSorterClicked = { sorterDialogVisible = true }
            )
        }
        CompetitionOrderDialog(
            visible = sorterDialogVisible,
            onChange = setOrder,
            onDismissRequest = { sorterDialogVisible = false },
            options = listOf(
                "title_asc",
                "title_desc",
                "fee_asc",
                "fee_desc"
            ),
            order = order
        )
        CompetitionFilterDialog(
            visible = filterDialogVisible,
            onDismissRequest = { filterDialogVisible = false },
            onChange = setFilter,
            options = filter.ifEmpty {
                mapOf(
                    "category" to listOf("national", "international").associateWith { false },
                    "organizer" to listOf(
                        "government",
                        "university",
                        "company"
                    ).associateWith { false },
                    "theme" to stringArrayResource(R.array.text_competition_theme_list).associateWith { false }
                )
            }
        )
    }
}

@Composable
fun isOrderFilterBtnVisible(order: String, filter: Map<String, Map<String, Boolean>>): Boolean {
    return remember(order, filter) {
        order.isNotBlank() || filter
            .mapValues { options -> options.value.filterValues { it } }
            .filterValues { it.isNotEmpty() }
            .isNotEmpty()
    }
}
