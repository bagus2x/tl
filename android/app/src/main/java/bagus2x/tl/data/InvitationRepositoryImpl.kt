package bagus2x.tl.data

import bagus2x.tl.data.remote.InvitationRemoteDataSource
import bagus2x.tl.domain.model.Invitation
import bagus2x.tl.domain.repository.InvitationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

class InvitationRepositoryImpl(
    private val remoteDataSource: InvitationRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : InvitationRepository {

    override suspend fun create(inviteeId: Long, description: String, file: File?) {
        withContext(dispatcher) {
            remoteDataSource.create(inviteeId, description, file)
        }
    }

    override suspend fun respond(invitationId: Long, response: String, status: String) {
        withContext(dispatcher) {
            remoteDataSource.respond(invitationId, response, status)
        }
    }

    override suspend fun getInvitation(invitationId: Long): Invitation {
        return withContext(dispatcher) {
            remoteDataSource.getInvitation(invitationId).asModel()
        }
    }
}
