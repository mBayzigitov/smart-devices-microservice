package ru.handh.userservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class DeleteAccountDto(
    @field:NotBlank(message = "Access token is required")
    @JsonProperty("accessToken")
    private val _access_token: String?,

    @field:NotBlank(message = "Password is required")
    @JsonProperty("password")
    private val _password: String?
) {
    val accessToken: String
        get() = _access_token!!

    val password: String
        get() = _password!!
}
