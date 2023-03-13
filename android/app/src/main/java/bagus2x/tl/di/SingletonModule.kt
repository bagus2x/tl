package bagus2x.tl.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import bagus2x.tl.data.*
import bagus2x.tl.data.local.*
import bagus2x.tl.data.remote.*
import bagus2x.tl.data.utils.HttpClient
import bagus2x.tl.data.utils.TlDatabase
import bagus2x.tl.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    private val Context.dataStore by preferencesDataStore(name = "tl")

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext
        context: Context
    ): TlDatabase {
        return Room.databaseBuilder(context, TlDatabase::class.java, "tl.db").build()
    }

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        @ApplicationContext
        context: Context
    ): AuthLocalDataSource {
        return AuthLocalDataSource(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideHttpClient(authLocalDataSource: AuthLocalDataSource): HttpClient {
        return HttpClient(authLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authLocalDataSource: AuthLocalDataSource,
        client: HttpClient,
        database: TlDatabase,
        dispatcher: CoroutineDispatcher
    ): AuthRepository {
        return AuthRepositoryImpl(
            localDataSource = authLocalDataSource,
            remoteDataSource = AuthRemoteDataSource(client),
            clearAllTables = { database.clearAllTables() },
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        database: TlDatabase,
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): MessageRepository {
        return MessageRepositoryImpl(
            remoteDataSource = MessageRemoteDataSource(client),
            localDataSource = database.messageLocalDataSource,
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: TlDatabase,
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserRepositoryImpl(
            localDataSource = database.userLocalDataSource,
            remoteDataSource = UserRemoteDataSource(client),
            dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCompetitionRepository(
        database: TlDatabase,
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): CompetitionRepository {
        return CompetitionRepositoryImpl(
            localDataSource = database.competitionLocalDataSource,
            remoteDataSource = CompetitionRemoteDataSource(client),
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideAnnouncementRepository(
        database: TlDatabase,
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): AnnouncementRepository {
        return AnnouncementRepositoryImpl(
            localDataSource = database.announcementLocalDataSource,
            remoteDataSource = AnnouncementRemoteDataSource(client),
            dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        database: TlDatabase,
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): NotificationRepository {
        return NotificationRepositoryImpl(
            localDataSource = database.notificationLocalDataSource,
            remoteDataSource = NotificationRemoteDataSource(client),
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideInvitationRepository(
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): InvitationRepository {
        return InvitationRepositoryImpl(
            remoteDataSource = InvitationRemoteDataSource(client),
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideTestimonyRepository(
        database: TlDatabase,
        client: HttpClient,
        dispatcher: CoroutineDispatcher
    ): TestimonyRepository {
        return TestimonyRepositoryImpl(
            localDataSource = database.testimonyLocalDataSource,
            remoteDataSource = TestimonyRemoteDataSource(client),
            dispatcher = dispatcher
        )
    }
}
