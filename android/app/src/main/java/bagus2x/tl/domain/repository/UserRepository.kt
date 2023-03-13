package bagus2x.tl.domain.repository

import bagus2x.tl.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    fun getUser(userId: Long): Flow<User?>

    fun getUsers(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<User>>

    suspend fun getFriends(userId: Long): List<User>

    suspend fun update(
        name: String, photo: File?, university: String,
        department: String, faculty: String, studyProgram: String,
        stream: String, batch: Int?, gender: String, age: Int?,
        bio: String, skills: List<String>,
        achievements: List<String>, certifications: List<String>,
        invitable: Boolean, location: String
    )

    suspend fun requestFriendship(userId: Long)

    suspend fun acceptFriendship(userId: Long)

    suspend fun cancelFriendship(userId: Long)

    suspend fun favorite(userId: Long)

    suspend fun unfavorite(userId: Long)

    suspend fun getFavorites(userId: Long): List<User>
}
