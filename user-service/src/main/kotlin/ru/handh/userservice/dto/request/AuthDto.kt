package ru.handh.userservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class AuthDto(
    @field:NotBlank(message = "Username is required")
    @JsonProperty("username")
    private val _username: String?,

    @field:NotBlank(message = "Password is required")
    @JsonProperty("password")
    private val _password: String?
) {
    val username: String
        get() = _username!!

    val password: String
        get() = _password!!
}
