package bagus2x.tl.di

import bagus2x.tl.data.*
import bagus2x.tl.data.utils.TlDatabase
import bagus2x.tl.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SingletonModule::class]
)
object FakeSingletonModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return FakeAuthRepository()
    }

    @Provides
    @Singleton
    fun provideMessageRepository(): MessageRepository {
        return FakeMessageRepository()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        authRepository: AuthRepository
    ): UserRepository {
        return FakeUserRepository(authRepository)
    }

    @Provides
    @Singleton
    fun provideCompetitionRepository(): CompetitionRepository {
        return FakeCompetitionRepository()
    }

    @Provides
    @Singleton
    fun provideAnnouncementRepository(): AnnouncementRepository {
        return FakeAnnouncementRepository()
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(): NotificationRepository {
        return FakeNotificationRepository()
    }

    @Provides
    @Singleton
    fun provideInvitationRepository(): InvitationRepository {
        return FakeInvitationRepository()
    }

    @Provides
    @Singleton
    fun provideTestimonyRepository(): TestimonyRepository {
        return FakeTestimonyRepository()
    }
}
