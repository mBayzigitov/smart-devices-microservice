package ru.handh.userservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class SignoutDto(
    @field:NotBlank(message = "Access token required")
    @JsonProperty("accessToken")
    private val _access_token: String?
) {
    val accessToken: String
        get() = _access_token!!
}
