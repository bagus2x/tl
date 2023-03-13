package bagus2x.tl.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bagus2x.tl.domain.model.Auth
import bagus2x.tl.presentation.announcement.form.AnnouncementFormScreen
import bagus2x.tl.presentation.announcement.list.AnnouncementListScreen
import bagus2x.tl.presentation.auth.signin.SignInScreen
import bagus2x.tl.presentation.auth.signup.SignUpScreen
import bagus2x.tl.presentation.chat.contact.ContactScreen
import bagus2x.tl.presentation.chat.detail.ChatDetailScreen
import bagus2x.tl.presentation.chat.list.ChatListScreen
import bagus2x.tl.presentation.competition.detail.CompetitionDetailScreen
import bagus2x.tl.presentation.competition.form.CompetitionFormScreen
import bagus2x.tl.presentation.competition.list.CompetitionListScreen
import bagus2x.tl.presentation.favorites.FavoritesScreen
import bagus2x.tl.presentation.invitation.detail.InvitationDetailScreen
import bagus2x.tl.presentation.invitation.detail.ResponseDetailScreen
import bagus2x.tl.presentation.invitation.form.InvitationFormScreen
import bagus2x.tl.presentation.invitation.form.ResponseFormScreen
import bagus2x.tl.presentation.notification.NotificationListScreen
import bagus2x.tl.presentation.search.SearchScreen
import bagus2x.tl.presentation.testimony.TestimonyFormScreen
import bagus2x.tl.presentation.user.edit.EditProfileScreen
import bagus2x.tl.presentation.user.list.FriendsListScreen
import bagus2x.tl.presentation.user.list.UsersListScreen
import bagus2x.tl.presentation.user.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    auth: Auth?,
    modifier: Modifier
) {
    val startDestination = when {
        auth == null -> "signin"
        auth.verified -> "announcement_list"
        else -> "signup"
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("signin") {
            SignInScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("chat_list") {
            ChatListScreen(navController)
        }
        composable(
            route = "chat_detail/{user_id}",
            arguments = listOf(navArgument("user_id") { type = NavType.LongType })
        ) {
            ChatDetailScreen(navController)
        }
        composable("contact") {
            ContactScreen(navController)
        }
        composable("competition_form") {
            CompetitionFormScreen(navController)
        }
        composable(
            route = "competition_detail/{competition_id}",
            arguments = listOf(navArgument("competition_id") { type = NavType.LongType })
        ) {
            CompetitionDetailScreen(navController)
        }
        composable("competition_list") {
            CompetitionListScreen(navController)
        }
        composable("announcement_list") {
            AnnouncementListScreen(navController)
        }
        composable("announcement_form") {
            AnnouncementFormScreen(navController)
        }
        composable(
            route = "user_detail/{user_id}",
            arguments = listOf(navArgument("user_id") { type = NavType.LongType })
        ) {
            ProfileScreen(
                navController = navController,
                authenticatedUser = false
            )
        }
        composable("user_list") {
            UsersListScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable(
            route = "friends/{user_id}",
            arguments = listOf(navArgument("user_id") { type = NavType.LongType })
        ) {
            FriendsListScreen(navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController)
        }
        composable(
            route = "favorites?user_id={user_id}",
            arguments = listOf(navArgument("user_id") { nullable = true })
        ) {
            FavoritesScreen(navController)
        }
        composable("notifications") {
            NotificationListScreen(navController)
        }
        composable(
            route = "invitation_form/{user_id}",
            arguments = listOf(navArgument("user_id") { type = NavType.LongType })
        ) {
            InvitationFormScreen(navController)
        }
        composable(
            route = "invitation_detail/{invitation_id}",
            arguments = listOf(navArgument("invitation_id") { type = NavType.LongType })
        ) {
            InvitationDetailScreen(navController)
        }
        composable(
            route = "response_form/{invitation_id}/{accepted}",
            arguments = listOf(
                navArgument("invitation_id") { type = NavType.LongType },
                navArgument("accepted") { type = NavType.BoolType }
            )
        ) {
            ResponseFormScreen(navController)
        }
        composable(
            route = "response_detail/{invitation_id}",
            arguments = listOf(navArgument("invitation_id") { type = NavType.LongType })
        ) {
            ResponseDetailScreen(navController)
        }
        composable(
            route = "testimony_form/{user_id}",
            arguments = listOf(navArgument("user_id") { type = NavType.LongType })
        ) {
            TestimonyFormScreen(navController)
        }
        composable(
            route = "search/{type}?user_id={user_id}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("user_id") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            SearchScreen(
                navController = navController,
                type = it.arguments?.getString("type") ?: ""
            )
        }
    }
}
