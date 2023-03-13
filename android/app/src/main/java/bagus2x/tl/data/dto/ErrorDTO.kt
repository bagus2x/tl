package bagus2x.tl.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: Int
)
