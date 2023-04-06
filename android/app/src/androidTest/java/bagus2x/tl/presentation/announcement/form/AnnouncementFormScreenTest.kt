package bagus2x.tl.presentation.announcement.form

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import bagus2x.tl.di.SingletonModule
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.Scaffold
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@UninstallModules(SingletonModule::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AnnouncementFormScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @Test
    fun add_announcement_shows_error_when_descriptions_is_blank() {
        composeRule.onNodeWithTag(Tag.TF_DESCRIPTION).assertExists().performTextInput("")
        composeRule.onNodeWithTag(Tag.BTN_ACTION).performClick()
    }
}
