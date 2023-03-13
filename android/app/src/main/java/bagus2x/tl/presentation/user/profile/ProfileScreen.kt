package bagus2x.tl.presentation.user.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Announcement
import bagus2x.tl.domain.model.Testimony
import bagus2x.tl.domain.model.User
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.AnnouncementList
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TestimonyList
import bagus2x.tl.presentation.common.components.TopBar
import bagus2x.tl.presentation.user.components.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    authenticatedUser: Boolean = true
) {
    val showSnackbar = LocalShowSnackbar.current
    val snackbar by viewModel.snackbar.collectAsState()
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
            viewModel.snackbarConsumed()
        }
    }

    val user by viewModel.user.collectAsState()
    val testimonies by viewModel.testimonies.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    ProfileScreen(
        authenticatedUser = authenticatedUser,
        user = user,
        testimonies = testimonies,
        announcements = announcements,
        navigateToFriends = {
            user?.id?.let { userId ->
                navController.navigate("friends/$userId")
            }
        },
        navigateToFavorites = {
            user?.id?.let { userId ->
                navController.navigate("favorites?user_id=$userId")
            }
        },
        signOut = viewModel::signOut,
        navigateToEdit = {
            navController.navigate("edit_profile")
        },
        navigateUp = navController::navigateUp,
        navigateToInvitationForm = {
            user?.id?.let { userId ->
                navController.navigate("invitation_form/$userId")
            }
        },
        requestFriendship = viewModel::friendship,
        navigateToTestimonyForm = {
            user?.id?.let { userId ->
                navController.navigate("testimony_form/$userId")
            }
        }
    )
}

@Composable
fun ProfileScreen(
    authenticatedUser: Boolean,
    user: User?,
    testimonies: List<Testimony>,
    announcements: List<Announcement>,
    navigateToEdit: (() -> Unit)? = null,
    signOut: (() -> Unit)? = null,
    navigateToFriends: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateUp: (() -> Unit)? = null,
    navigateToInvitationForm: (() -> Unit)? = null,
    requestFriendship: (() -> Unit)? = null,
    navigateToTestimonyForm: (() -> Unit)? = null
) {
    if (authenticatedUser) {
        ProfileScreen(
            user = user,
            testimonies = testimonies,
            announcements = announcements,
            navigateToFriends = navigateToFriends,
            navigateToFavorites = navigateToFavorites,
            signOut = signOut,
            navigateToEdit = navigateToEdit
        )
    } else {
        ProfileScreen(
            user = user,
            testimonies = testimonies,
            announcements = announcements,
            navigateToFriends = navigateToFriends,
            navigateToFavorites = navigateToFavorites,
            navigateUp = navigateUp,
            navigateToInvitationForm = navigateToInvitationForm,
            requestFriendship = requestFriendship,
            navigateToTestimonyForm = navigateToTestimonyForm
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    user: User?,
    testimonies: List<Testimony>,
    announcements: List<Announcement>,
    navigateToEdit: (() -> Unit)? = null,
    signOut: (() -> Unit)? = null,
    navigateToFriends: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateUp: (() -> Unit)? = null,
    navigateToInvitationForm: (() -> Unit)? = null,
    requestFriendship: (() -> Unit)? = null,
    navigateToTestimonyForm: (() -> Unit)? = null
) {
    Scaffold(
        modifier = Modifier.testTag(Tag.PROFILE_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_profile),
                onBackClicked = navigateUp,
                onEditClicked = navigateToEdit,
                onSignOutClicked = signOut
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (user != null) {
                ProfileHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    photo = user.photo,
                    name = user.name,
                    friends = user.friends,
                    likes = user.likes,
                    bio = user.bio,
                    friendshipStatus = user.status,
                    onFriendsClicked = navigateToFriends,
                    onLikesClicked = navigateToFavorites,
                    onInvitationClicked = if (user.invitable) navigateToInvitationForm else null,
                    onTestimonyClicked = navigateToTestimonyForm,
                    onReqFriendshipClicked = requestFriendship
                )
                Column(modifier = Modifier.weight(1F)) {
                    val pagerState = rememberPagerState(initialPage = 0)
                    val scope = rememberCoroutineScope()
                    val tabTitles = remember {
                        listOf(
                            R.string.text_personal_data,
                            R.string.text_testimony,
                            R.string.text_post,
                        )
                    }
                    ProfileTabs(
                        modifier = Modifier.fillMaxWidth(),
                        selectedTabIndex = pagerState.currentPage,
                        onClickTab = { index ->
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        titles = tabTitles
                    )
                    HorizontalPager(
                        state = pagerState,
                        count = tabTitles.size,
                        modifier = Modifier.fillMaxHeight()
                    ) { page: Int ->
                        when (page) {
                            0 -> PersonalData(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .fillMaxSize()
                                    .padding(vertical = 16.dp),
                                university = user.university,
                                batch = user.batch,
                                department = user.department,
                                studyProgram = user.studyProgram,
                                stream = user.stream,
                                gender = user.gender,
                                age = user.age,
                                city = user.location,
                                skills = user.skills,
                                achievements = user.achievements,
                                certifications = user.certifications
                            )
                            1 -> {
                                TestimonyList(
                                    modifier = Modifier.fillMaxSize(),
                                    testimonies = testimonies
                                )
                            }
                            2 -> {
                                AnnouncementList(
                                    modifier = Modifier.fillMaxSize(),
                                    announcements = announcements
                                )
                            }
                        }
                    }
                }
            }
        }
        if (user == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
