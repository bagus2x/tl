package bagus2x.tl.data.local

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import bagus2x.tl.data.dto.CompetitionDTO
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Dao
abstract class CompetitionLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(competition: CompetitionDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(competitions: List<CompetitionDTO>)

    @Query("SELECT * FROM competition WHERE id = :competitionId")
    abstract fun getCompetition(
        competitionId: Long
    ): Flow<CompetitionDTO?>

    @RawQuery(observedEntities = [CompetitionDTO::class])
    abstract fun getCompetitions(
        query: SupportSQLiteQuery
    ): Flow<List<CompetitionDTO>>

    fun getCompetitions(
        query: String,
        order: String,
        filter: Map<String, List<String>>
    ): Flow<List<CompetitionDTO>> {
        val sql = buildQuery(query, order, filter)
        return getCompetitions(SimpleSQLiteQuery(sql))
    }
}

private fun buildQuery(query: String, order: String, filter: Map<String, List<String>>): String {
    return buildString {
        append("SELECT * FROM competition")
        if (query.isNotBlank()) {
            if (!contains("WHERE")) append(" WHERE ")
            if (query.isNotBlank()) {
                append(" LOWER(title) LIKE '%${query.lowercase()}%' ")
                append(" OR LOWER(description) LIKE '%${query.lowercase()}%' ")
                append(" OR LOWER(theme) LIKE '%${query.lowercase()}%' ")
            }
        }
        val filterArgs = filter
            .filterNot { it.value.isEmpty() }
            .map { (key, value) ->
                when (key) {
                    "category" -> {
                        val v = value.joinToString(",") { "'${it.lowercase()}'" }
                        " LOWER(category) in ($v)"
                    }
                    "organizer" -> {
                        val v = value.joinToString(",") { "'${it.lowercase()}'" }
                        " LOWER(organizer) in ($v)"
                    }
                    "theme" -> {
                        val v = value.joinToString(",") { "'${it.lowercase()}'" }
                        " LOWER(theme) in ($v)"
                    }
                    else -> null
                }
            }
            .filterNotNull()
        if (filterArgs.isNotEmpty()) {
            if (!contains("WHERE")) append(" WHERE ")
            append(filterArgs.joinToString(" AND "))
        }
        if (order == "title_asc") append(" ORDER BY title ASC, created_at DESC")
        if (order == "title_desc") append(" ORDER BY title DESC, created_at DESC")
        if (order == "fee_asc") append(" ORDER BY minimum_fee ASC, created_at DESC")
        if (order == "fee_desc") append(" ORDER BY minimum_fee DESC, created_at DESC")
        if (order.isBlank()) append(" ORDER BY created_at DESC")

        Timber.d("QUERY DEBUG: $this")
    }
}
