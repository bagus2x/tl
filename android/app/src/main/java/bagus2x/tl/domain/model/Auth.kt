package bagus2x.tl.domain.model

data class Auth(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
    val verified: Boolean
)
