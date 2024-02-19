package ru.handh.userservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RegisterDto(
    @field:NotBlank(message = "Name must not be blank")
    @JsonProperty("name")
    private val _name: String?,

    @field:NotNull(message = "Username must be not null value and contain at least 5 characters")
    @field:Size(min = 5, max = 20, message = "Username must be from 5 to 20 characters long")
    @JsonProperty("username")
    private val _username: String?,

    @field:NotNull(message = "Password must be not null value and contain at least 7 characters")
    @field:Size(min = 7, max = 15, message = "Password must be from 7 to 15 characters long")
    val password: String?,

    @field:NotNull(message = "Password confirmation must be not null value")
    val confirmPassword: String?
) {
    val username: String
       get() = _username!!

    val name: String
        get() = _name!!
}
