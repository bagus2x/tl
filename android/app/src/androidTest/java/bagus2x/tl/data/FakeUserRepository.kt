package bagus2x.tl.data

import bagus2x.tl.TestUtils
import bagus2x.tl.data.dto.UserDTO
import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.repository.AuthRepository
import bagus2x.tl.domain.repository.UserRepository
import bagus2x.tl.presentation.common.Utils
import kotlinx.coroutines.flow.*
import java.io.File

val Users = LongArray(1000) { it + 1L }
    .map {
        val name = TestUtils.getRandomWords(2)
        UserDTO(
            id = it,
            name = TestUtils.getRandomWords(2),
            email = TestUtils.getRandomWords(2).replace(" ", "") + "@gmail.com",
            photo = Utils.profile(name),
            university = null,
            faculty = null,
            department = null,
            studyProgram = null,
            stream = null,
            batch = null,
            gender = null,
            age = null,
            bio = null,
            skills = listOf(),
            achievements = listOf(),
            certifications = listOf(),
            invitable = true,
            location = null,
            favorite = it % 2 == 0L,
            rating = 0.0f,
            votes = 0,
            likes = 0,
            friends = 0,
            friendshipStatus = null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
    .sortedByDescending { it.createdAt }

class FakeUserRepository(
    private val authRepository: AuthRepository
) : UserRepository {
    private val users = MutableStateFlow(Users)

    override fun getUser(userId: Long): Flow<User?> {
        return users.map { it.find { it.id == userId }?.asModel() }
    }

    override fun getUsers(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<User>> {
        return users.map { it.filter { it.name.contains(query, true) }.map { it.asModel() } }
    }

    override suspend fun getFriends(userId: Long): List<User> {
        return users.value.map { it.asModel() }
    }

    override suspend fun update(
        name: String,
        photo: File?,
        university: String,
        department: String,
        faculty: String,
        studyProgram: String,
        stream: String,
        batch: Int?,
        gender: String,
        age: Int?,
        bio: String,
        skills: List<String>,
        achievements: List<String>,
        certifications: List<String>,
        invitable: Boolean,
        location: String
    ) {
        val id = authRepository.getAuth().first()?.userId
        users.update { users ->
            users.map { user ->
                if (user.id == id) user.copy(
                    name = name,
                    university = university,
                    department = department,
                    faculty = faculty,
                    studyProgram = studyProgram,
                    stream = stream,
                    batch = batch,
                    gender = gender,
                    age = age,
                    bio = bio,
                    skills = skills,
                    achievements = achievements,
                    certifications = certifications,
                    invitable = invitable,
                    location = location
                )
                else user
            }
        }
    }

    override suspend fun requestFriendship(userId: Long) {
        users.update { users ->
            users.map { user ->
                if (user.id == userId) user.copy(
                    friendshipStatus = "friendship_requested"
                )
                else user
            }
        }
    }

    override suspend fun acceptFriendship(userId: Long) {
        val authId = authRepository.getAuth().first()?.userId
        users.update { users ->
            users.map { user ->
                if (user.id == userId) user.copy(
                    friendshipStatus = "friendship_accepted",
                    friends = user.friends + 1
                )
                else if (user.id == authId) user.copy(
                    friendshipStatus = "friendship_accepted",
                    friends = user.friends + 1
                )
                else user
            }
        }
    }

    override suspend fun cancelFriendship(userId: Long) {
        val authId = authRepository.getAuth().first()?.userId
        users.update { users ->
            users.map { user ->
                val friends =
                    if (user.friendshipStatus == "friendship_accepted")
                        user.friends - 1
                    else
                        user.friends
                if (user.id == userId) user.copy(
                    friends = friends
                )
                else if (user.id == authId) user.copy(
                    friendshipStatus = "friendship_accepted",
                    friends = friends
                )
                else user
            }
        }
    }

    override suspend fun favorite(userId: Long) {
        users.update { users -> users.map { if (it.id == userId) it.copy(favorite = true) else it } }
    }

    override suspend fun unfavorite(userId: Long) {
        users.update { users -> users.map { if (it.id == userId) it.copy(favorite = false) else it } }
    }

    override suspend fun getFavorites(userId: Long): List<User> {
        return users
            .map { users -> users.filter { it.favorite }.map { it.asModel() } }
            .firstOrNull() ?: emptyList()
    }
}
