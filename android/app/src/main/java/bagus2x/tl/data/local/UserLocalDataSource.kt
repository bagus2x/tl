package bagus2x.tl.data.local

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import bagus2x.tl.data.dto.UserDTO
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Dao
abstract class UserLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(user: UserDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(users: List<UserDTO>)

    @Query("SELECT * FROM user WHERE id = :userId")
    abstract fun getUser(userId: Long): Flow<UserDTO?>

    @RawQuery(observedEntities = [UserDTO::class])
    abstract fun getUsers(query: SupportSQLiteQuery): Flow<List<UserDTO>>

    fun getUsers(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<UserDTO>> {
        val sql = buildQuery(query, order, filter)
        return getUsers(SimpleSQLiteQuery(sql, arrayOf<Any>()))
    }
}

private fun buildQuery(query: String, order: String, filter: Map<String, List<String>>): String {
    return buildString {
        append("SELECT * FROM user")
        if (query.isNotBlank()) {
            if (!contains("WHERE")) append(" WHERE ")
            if (query.isNotBlank()) {
                append(" LOWER(name) LIKE '%${query.lowercase()}%' ")
                append(" OR LOWER(bio) LIKE '%${query.lowercase()}%' ")
            }
        }
        val filterArgs = filter
            .filterNot { it.value.isEmpty() }
            .map { (key, value) ->
                when (key) {
                    "study_program" -> {
                        val v = value.joinToString(",") { "'${it.lowercase()}'" }
                        " LOWER(study_program) in ($v)"
                    }
                    "stream" -> {
                        val v = value.joinToString(",") { "'${it.lowercase()}'" }
                        " LOWER(stream) in ($v)"
                    }
                    "batch" -> " batch in (${value.joinToString(",")})"
                    else -> null
                }
            }
            .filterNotNull()
        if (filterArgs.isNotEmpty()) {
            if (!contains("WHERE")) append(" WHERE ")
            append(filterArgs.joinToString(" AND "))
        }
        if (order == "name_asc") append(" ORDER BY name ASC")
        if (order == "name_desc") append(" ORDER BY name DESC")
        if (order == "batch_asc") append(" ORDER BY batch ASC")
        if (order == "batch_desc") append(" ORDER BY batch DESC")
        if (order.isBlank()) append(" ORDER BY created_at DESC")

        Timber.d("QUERY DEBUG: $this")
    }
}
