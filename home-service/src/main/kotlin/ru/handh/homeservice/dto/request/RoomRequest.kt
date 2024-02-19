package ru.handh.homeservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class RoomRequest(
    @field:NotBlank(message = "Name must be neither null value nor empty string")
    @JsonProperty("name")
    private val _name: String?
) {
    val name: String
        get() = _name!!
}