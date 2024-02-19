package ru.handh.userservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotBlank

data class RefreshDto(
    @field:NotBlank(message = "Access token is required")
    @JsonProperty("accessToken")
    private val _accessToken: String?,

    @field:NotBlank(message = "Refresh token is required")
    @JsonProperty("refreshToken")
    private val _refreshToken: String?
) {
    val refreshToken: UUID
        get() = UUID.fromString(_refreshToken)

    val accessToken: String
        get() = _accessToken!!
}
