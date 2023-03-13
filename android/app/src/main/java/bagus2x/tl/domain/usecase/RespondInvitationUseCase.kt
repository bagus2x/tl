package bagus2x.tl.domain.usecase

import bagus2x.tl.domain.repository.InvitationRepository

class RespondInvitationUseCase(
    private val invitationRepository: InvitationRepository
) {

    suspend operator fun invoke(invitationId: Long, response: String, accepted: Boolean) {
        val status = if (accepted) "invitation_accepted" else "invitation_declined"
        require(response.length < 500) {
            "Deskripsi respons wajib kurang dari 500 karakter"
        }
        invitationRepository.respond(invitationId, response, status)
    }
}
