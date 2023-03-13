package bagus2x.tl.data

import bagus2x.tl.data.local.UserLocalDataSource
import bagus2x.tl.data.remote.UserRemoteDataSource
import bagus2x.tl.data.utils.networkBoundResource
import bagus2x.tl.domain.model.User
import bagus2x.tl.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
    private val dispatcher: CoroutineDispatcher,
) : UserRepository {

    override fun getUser(userId: Long): Flow<User?> {
        return networkBoundResource(
            local = {
                localDataSource.getUser(userId)
            },
            remote = {
                remoteDataSource.getUser(userId)
            },
            update = { res ->
                localDataSource.save(res)
            }
        )
            .map { it?.asModel() }
            .flowOn(dispatcher)
    }

    override fun getUsers(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<User>> {
        return networkBoundResource(
            local = {
                localDataSource.getUsers(query, order, filter)
            },
            remote = {
                remoteDataSource.getUsers(query, order, filter)
            },
            update = { res ->
                localDataSource.save(res)
            }
        )
            .map { it.map { dto -> dto.asModel() } }
            .flowOn(dispatcher)
    }

    override suspend fun getFriends(userId: Long): List<User> {
        return remoteDataSource.getFriends(userId).map { it.asModel() }
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
        withContext(dispatcher) {
            val res = remoteDataSource.update(
                name,
                photo,
                university,
                department,
                faculty,
                studyProgram,
                stream,
                batch,
                gender,
                age,
                bio,
                skills,
                achievements,
                certifications,
                invitable,
                location
            )
            localDataSource.save(res)
        }
    }

    override suspend fun requestFriendship(userId: Long) {
        withContext(dispatcher) {
            remoteDataSource.requestFriendship(userId)
            val user = localDataSource
                .getUser(userId)
                .firstOrNull()
                ?: return@withContext
            localDataSource
                .save(
                    user.copy(friendshipStatus = "friendship_requested")
                )
        }
    }

    override suspend fun acceptFriendship(userId: Long) {
        withContext(dispatcher) {
            remoteDataSource
                .acceptFriendship(userId)
            val user = localDataSource
                .getUser(userId)
                .firstOrNull()
                ?: return@withContext
            localDataSource.save(
                user.copy(friendshipStatus = "friendship_accepted")
            )
        }
    }

    override suspend fun cancelFriendship(userId: Long) {
        withContext(dispatcher) {
            remoteDataSource.cancelFriendship(userId)
            val user = localDataSource
                .getUser(userId)
                .firstOrNull()
                ?: return@withContext
            val isFriend = user.friendshipStatus == "friendship_accepted"
            val friends = if (isFriend) user.friends - 1 else user.friends
            localDataSource.save(user.copy(friendshipStatus = null, friends = friends))
        }
    }

    override suspend fun favorite(userId: Long) {
        withContext(dispatcher) {
            remoteDataSource.favorite(userId)
            val user = localDataSource
                .getUser(userId)
                .firstOrNull()
                ?: return@withContext
            localDataSource
                .save(user.copy(favorite = true))
        }
    }

    override suspend fun unfavorite(userId: Long) {
        withContext(dispatcher) {
            remoteDataSource
                .unfavorite(userId)
            val user = localDataSource
                .getUser(userId).firstOrNull()
                ?: return@withContext
            localDataSource
                .save(user.copy(favorite = false))
        }
    }

    override suspend fun getFavorites(
        userId: Long
    ): List<User> {
        return withContext(dispatcher) {
            remoteDataSource
                .getFavorites(userId)
                .map { it.asModel() }
        }
    }
}
