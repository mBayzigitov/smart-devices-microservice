package ru.handh.homeservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class HomeRequest(
    /*
        Additional validation requirements:
            - name must contain at least 1 character
            - name must be no more than 50 characters
            - address must be no more than 300 characters
     */

    @field:NotNull(message = "User id is required")
    @JsonProperty("userId")
    private val _userId: Int?,

    // NotBlank checks if value is null or its trimmed size is zero
    @field:NotBlank(message = "Name must be neither null value nor empty string")
    @field:Length(max = 50, message = "Name must contain no more than 50 characters")
    @JsonProperty("name")
    private val _name: String?,

    @field:Length(max = 300, message = "Name must contain no more than 300 characters")
    val address: String?
) {
    val name: String
        get() = _name!!

    val userId: Int
        get() = _userId!!
}