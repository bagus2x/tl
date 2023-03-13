package bagus2x.tl.uitest

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import bagus2x.tl.R.string.*
import bagus2x.tl.TestUtils
import bagus2x.tl.data.Competitions
import bagus2x.tl.data.Messages
import bagus2x.tl.data.Users
import bagus2x.tl.di.SingletonModule
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@UninstallModules(SingletonModule::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class EndToEndTesting {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val ctx: Context get() = composeRule.activity

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun sign_up() = composeRule.run {
        onNodeWithTag(Tag.BTN_NAVIGATE_TO_SIGN_UP)
            .performClick()
        onNodeWithTag(Tag.TF_NAME)
            .performTextInput("Tubagus Saifulloh")
        onNodeWithTag(Tag.TF_EMAIL)
            .performTextInput("tubagussaifulloh@gmail.com")
        onNodeWithTag(Tag.TF_PASSWORD)
            .performTextInput("bismillah123")
        onNodeWithTag(Tag.BTN_SIGNUP)
            .performClick()
        onNodeWithTag(Tag.TF_VERIFICATION_CODE)
            .performTextInput("8080")
        onNodeWithTag(Tag.BTN_SUBMIT)
            .performClick()
        onNodeWithTag(Tag.ANNOUNCEMENT_LIST_SCREEN)
            .assertExists()
        Unit
    }

    @Test
    fun sign_in() = composeRule.run {
        onNodeWithTag(Tag.TF_EMAIL)
            .performTextInput("bagus@gmail.com")
        onNodeWithTag(Tag.TF_PASSWORD)
            .performTextInput("tubagussaifulloh")
        onNodeWithTag(Tag.BTN_SIGNIN).performClick()
        onNodeWithTag(Tag.TF_VERIFICATION_CODE)
            .performTextInput("8080")
        onNodeWithTag(Tag.BTN_SUBMIT).performClick()
        onNodeWithTag(Tag.ANNOUNCEMENT_LIST_SCREEN).assertExists()
        Unit
    }

    @Test
    fun add_announcement() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_ADD).performClick()
        onNodeWithTag(Tag.ANNOUNCEMENT_FORM_SCREEN).assertExists()
        val description = TestUtils.getRandomWords(10)
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performTextInput(description)
        onNodeWithTag(Tag.BTN_ACTION).performClick()
        onNodeWithTag(Tag.ANNOUNCEMENT_LIST_SCREEN).assertExists()
        onNodeWithText(description).assertExists()
        Unit
    }

    @Test
    fun publish_competition() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_COMPETITIONS).performClick()
        onNodeWithTag(Tag.COMPETITION_LIST_SCREEN).assertExists()
        onNodeWithTag(Tag.BTN_ADD).performClick()
        onNodeWithTag(Tag.COMPETITION_FORM_SCREEN).assertExists()
        val title = "Competition X"
        onNodeWithText(str(text_national))
            .performClick()
        onNodeWithText(str(text_university))
            .performScrollTo()
            .performClick()
        onNodeWithTag(Tag.TF_ORGANIZER_NAME)
            .performScrollTo()
            .performTextInput(TestUtils.getRandomWords(4))
        onNodeWithTag(Tag.TF_ORGANIZER_NAME)
            .performScrollTo()
            .performImeAction()
        onNodeWithTag(Tag.TF_THEME)
            .performScrollTo()
            .performTextInput(TestUtils.getRandomWords(2))
        onNodeWithTag(Tag.TF_THEME)
            .performImeAction()
        onNodeWithTag(Tag.TF_TITLE)
            .performScrollTo()
            .performTextInput(title)
        onNodeWithTag(Tag.TF_TITLE)
            .performImeAction()
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performScrollTo()
            .performTextInput(TestUtils.getRandomWords(10))
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performImeAction()
        onNodeWithTag(Tag.TF_CITY)
            .performScrollTo()
            .performTextInput(TestUtils.getRandomWords(4))
        onNodeWithTag(Tag.TF_CITY)
            .performImeAction()
        onNodeWithTag(Tag.TF_COUNTRY)
            .performScrollTo()
            .performTextInput(TestUtils.getRandomWords(1))
        onNodeWithTag(Tag.TF_COUNTRY)
            .performImeAction()
        onNodeWithTag(Tag.TF_DEADLINE)
            .performScrollTo()
            .performClick()
        onAllNodesWithTag("dialog_date_next_month").onFirst().performClick()
        onAllNodesWithTag("dialog_date_selection_28").onFirst().performClick()
        onNodeWithText("OK").performClick()
        onNodeWithText("OK").performClick()
        onNodeWithTag(Tag.TF_DEADLINE)
            .performImeAction()
        onNodeWithTag(Tag.TF_MIN)
            .performScrollTo()
            .performTextInput("50000")
        onNodeWithTag(Tag.TF_MIN)
            .performImeAction()
        onNodeWithTag(Tag.TF_MAX)
            .performScrollTo()
            .performTextInput("100000")
        onNodeWithTag(Tag.TF_MAX)
            .performImeAction()
        onNodeWithTag(Tag.TF_URL)
            .performScrollTo()
            .performTextInput("https://google.com")
        onNodeWithTag(Tag.TF_URL)
            .performImeAction()
        onNodeWithTag(Tag.BTN_ACTION)
            .performClick()
        onNodeWithTag(Tag.COMPETITION_LIST_SCREEN)
            .assertExists()
        onNodeWithText(title).assertExists()
        Unit
    }

    @Test
    fun mark_competition_as_favorite() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_COMPETITIONS).performClick()
        onNodeWithTag(Tag.COMPETITION_LIST_SCREEN).assertExists()
        val competition = Competitions.find { !it.favorite }!!
        onNodeWithText(competition.title)
            .assertExists()
            .performClick()
        onNodeWithTag(Tag.BTN_UNFAVORITE)
            .assertExists()
            .performClick()
        onNodeWithTag(Tag.BTN_FAVORITE)
            .assertExists()
            .performClick()
        onNodeWithTag(Tag.BTN_UNFAVORITE)
            .assertExists()
        Unit
    }

    @Test
    fun send_invitation() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_USERS)
            .performClick()
        val user = Users[2]
        onNodeWithText(user.name).performClick()
        onNodeWithTag(Tag.PROFILE_SCREEN).assertExists()
        onNodeWithTag(Tag.BTN_INVITE).performClick()
        onNodeWithTag(Tag.INVITATION_FORM_SCREEN).assertExists()
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performTextInput(TestUtils.getRandomWords(50))
        onNodeWithTag(Tag.BTN_ACTION)
            .performClick()
        onNodeWithText(str(text_invitation_sent))
            .assertExists()
        Unit
    }

    @Test
    fun create_testimony() = composeRule.run {
        sign_in()
        val user = Users[2]
        onNodeWithTag(Tag.BTN_USERS).performClick()
        onNodeWithTag(Tag.USERS_LIST_SCREEN).assertExists()
        onNodeWithText(user.name).performClick()
        onNodeWithTag(Tag.PROFILE_SCREEN).assertExists()
        onNodeWithTag(Tag.BTN_ADD_TESTIMONY).performClick()
        onNodeWithTag(Tag.TESTIMONY_FORM_SCREEN).assertExists()
        onNodeWithTag("${Tag.BTN_STAR}-4").performClick()
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performTextInput(TestUtils.getRandomWords(10))
        onNodeWithTag(Tag.BTN_ACTION).performClick()
        val successText = str(text_testimony_sent)
        onNodeWithText(successText)
            .assertExists()
        Unit
    }

    @Test
    fun respond_invitation() = composeRule.run {
        sign_in()
        onNodeWithContentDescription(str(text_notification))
            .performClick()
        onNodeWithText(str(text_invitation_requested))
            .performClick()
        onNodeWithTag(Tag.BTN_ACTION).performClick()
        onNodeWithText(str(text_accept)).performClick()
        onNodeWithTag(Tag.RESPONSE_FORM_SCREEN).assertExists()
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performTextInput(TestUtils.getRandomWords(50))
        onNodeWithTag(Tag.BTN_ACTION).performClick()
        onNodeWithText(str(text_response_sent))
            .assertExists()
        Unit
    }

    @Test
    fun request_friendship() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_USERS).performClick()
        val user = Users[2]
        onNodeWithText(user.name).performClick()
        onNodeWithTag(Tag.PROFILE_SCREEN).assertExists()
        onNodeWithTag(Tag.BTN_REQ_FRIEND).performClick()
        onNodeWithText(str(text_requested))
            .assertExists()
        Unit
    }

    @Test
    fun send_message() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_CHAT).performClick()
        val message = Messages.first()
        onNodeWithText(message.description).performClick()
        onNodeWithTag(Tag.TF_DESCRIPTION)
            .performTextInput(TestUtils.getRandomWords(2))
    }

    @Test
    fun edit_profile() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_PROFILE).performClick()
        onNodeWithContentDescription(str(text_see_more))
            .performClick()
        onNodeWithText(str(text_edit_profile))
            .performClick()
        onNodeWithTag(Tag.TF_NAME).performTextInput("John Jackson")
        onNodeWithTag(Tag.BTN_ACTION).performClick()
        onNodeWithText(str(text_profile_saved))
            .assertExists()
        Unit
    }

    @Test
    fun sign_out() = composeRule.run {
        sign_in()
        onNodeWithTag(Tag.BTN_PROFILE).performClick()
        onNodeWithContentDescription(str(text_see_more))
            .performClick()
        onNodeWithText(str(text_signout)).performClick()
        onNodeWithTag(Tag.SIGNIN_SCREEN).assertExists()
        Unit
    }

    private fun str(id: Int) = ctx.getString(id)
}
