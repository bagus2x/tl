package bagus2x.tl.domain.usecase

import bagus2x.tl.data.FakeUserRepository
import bagus2x.tl.domain.model.Auth
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var editProfileUseCase: EditProfileUseCase

    @Before
    fun setup() {
        authRepository = mock()
        userRepository = FakeUserRepository(authRepository)
        editProfileUseCase = EditProfileUseCase(userRepository)

        `when`(authRepository.getAuth()).thenReturn(
            flow {
                emit(
                    Auth(
                        userId = 1,
                        accessToken = "token",
                        refreshToken = "token",
                        verified = true
                    )
                )
            }
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `edit profile throws exception when name is blank`() = runTest {
        editProfileUseCase(
            name = "",
            photo = null,
            university = "",
            faculty = "",
            department = "",
            studyProgram = "",
            stream = "",
            batch = null,
            gender = "",
            age = null,
            bio = "",
            skills = listOf(),
            achievements = listOf(),
            certifications = listOf(),
            invitable = false,
            location = ""
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `edit profile throws exception when photo is greater than 2mb`() = runTest {
        val file = mock<File>()
        `when`(file.length()).thenReturn(2 * 1024 * 1024)
        editProfileUseCase(
            name = "bagus",
            photo = file,
            university = "",
            faculty = "",
            department = "",
            studyProgram = "",
            stream = "",
            batch = null,
            gender = "",
            age = null,
            bio = "",
            skills = listOf(),
            achievements = listOf(),
            certifications = listOf(),
            invitable = false,
            location = ""
        )
    }

    @Test
    fun `edit profile success`() = runTest {
        val photo = mock<File>()
        `when`(photo.length()).thenReturn(1 * 1024 * 1024)
        editProfileUseCase(
            name = "bagus",
            photo = photo,
            university = "",
            faculty = "",
            department = "",
            studyProgram = "",
            stream = "",
            batch = null,
            gender = "",
            age = null,
            bio = "",
            skills = listOf(),
            achievements = listOf(),
            certifications = listOf(),
            invitable = false,
            location = ""
        )
        val user = userRepository.getUser(1).filterNotNull().first()
        assertEquals("bagus", user.name)
    }
}
