package bagus2x.tl.data.dto

import bagus2x.tl.domain.model.Auth
import com.auth0.android.jwt.JWT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthDTO(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("verified")
    val verified: Boolean
) {
    val userId: Long
        get() {
            val jwt = JWT(accessToken)
            val subject = jwt.subject
            requireNotNull(subject)
            return subject.toLong()
        }

    fun asModel(): Auth {
        return Auth(userId, accessToken, refreshToken, verified)
    }
}
