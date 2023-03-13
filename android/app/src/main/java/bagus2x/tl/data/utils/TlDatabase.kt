package bagus2x.tl.data.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import bagus2x.tl.data.local.*
import bagus2x.tl.data.dto.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [
        MessageDTO::class,
        UserDTO::class,
        CompetitionDTO::class,
        AnnouncementDTO::class,
        NotificationDTO::class,
        TestimonyDTO::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [Converters::class])
abstract class TlDatabase : RoomDatabase() {

    abstract val messageLocalDataSource: MessageLocalDataSource

    abstract val userLocalDataSource: UserLocalDataSource

    abstract val competitionLocalDataSource: CompetitionLocalDataSource

    abstract val announcementLocalDataSource: AnnouncementLocalDataSource

    abstract val notificationLocalDataSource: NotificationLocalDataSource

    abstract val testimonyLocalDataSource: TestimonyLocalDataSource
}

class Converters {

    @TypeConverter
    fun fromStringToListOfString(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromListOfStringToString(value: List<String>): String {
        return Json.encodeToString(value)
    }
}
