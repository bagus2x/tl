package bagus2x.tl.di

import bagus2x.tl.domain.repository.*
import bagus2x.tl.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideCheckAuthUseCase(authRepository: AuthRepository): GetAuthUseCase {
        return GetAuthUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
        return SignInUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetUsersUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository
    ): GetUsersUseCase {
        return GetUsersUseCase(userRepository, authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetUserUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): GetUserUseCase {
        return GetUserUseCase(authRepository, userRepository)
    }

    @Provides
    @ViewModelScoped
    fun providePublishCompetitionUseCase(competitionRepository: CompetitionRepository): PublishCompetitionUseCase {
        return PublishCompetitionUseCase(competitionRepository)
    }

    @Provides
    @ViewModelScoped
    fun getFriendsUseCase(userRepository: UserRepository): GetFriendsUseCase {
        return GetFriendsUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun getLastMessagesUseCase(
        messageRepository: MessageRepository
    ): GetLastMessagesUseCase {
        return GetLastMessagesUseCase(messageRepository)
    }

    @Provides
    @ViewModelScoped
    fun getMessagesUseCase(messageRepository: MessageRepository): GetMessagesUseCase {
        return GetMessagesUseCase(messageRepository)
    }

    @Provides
    @ViewModelScoped
    fun getCompetitionsUseCase(competitionRepository: CompetitionRepository): GetCompetitionsUseCase {
        return GetCompetitionsUseCase(competitionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCompetitionUseCase(competitionRepository: CompetitionRepository): GetCompetitionUseCase {
        return GetCompetitionUseCase(competitionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideEditProfileUseCase(userRepository: UserRepository): EditProfileUseCase {
        return EditProfileUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideAddAnnouncementUseCase(
        announcementRepository: AnnouncementRepository
    ): AddAnnouncementUseCase {
        return AddAnnouncementUseCase(announcementRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAnnouncements(
        announcementRepository: AnnouncementRepository
    ): GetAnnouncementsUseCase {
        return GetAnnouncementsUseCase(announcementRepository)
    }

    @Provides
    @ViewModelScoped
    fun getNotificationsUseCase(notificationRepository: NotificationRepository): GetNotificationsUseCase {
        return GetNotificationsUseCase(notificationRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFriendshipUseCase(userRepository: UserRepository): FriendUseCase {
        return FriendUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateTestimonyUseCase(testimonyRepository: TestimonyRepository): CreateTestimonyUseCase {
        return CreateTestimonyUseCase(testimonyRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetTestimoniesUseCase(
        testimonyRepository: TestimonyRepository
    ): GetTestimoniesUseCase {
        return GetTestimoniesUseCase(testimonyRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateInvitationUseCase(invitationRepository: InvitationRepository): CreateInvitationUseCase {
        return CreateInvitationUseCase(invitationRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetInvitationUseCase(
        invitationRepository: InvitationRepository
    ): GetInvitationUseCase {
        return GetInvitationUseCase(invitationRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRespondInvitationUseCase(invitationRepository: InvitationRepository): RespondInvitationUseCase {
        return RespondInvitationUseCase(invitationRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFavUserUseCase(userRepository: UserRepository): FavUserUseCase {
        return FavUserUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetFavUsersUseCase(userRepository: UserRepository): GetFavUsersUseCase {
        return GetFavUsersUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFavCompetitionUseCase(competitionRepository: CompetitionRepository): FavCompetitionUseCase {
        return FavCompetitionUseCase(competitionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetFavCompetitionsUseCase(competitionRepository: CompetitionRepository): GetFavCompetitionsUseCase {
        return GetFavCompetitionsUseCase(competitionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSendMessageUseCase(messageRepository: MessageRepository): SendMessageUseCase {
        return SendMessageUseCase(messageRepository)
    }
}
