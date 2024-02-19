package ru.handh.deviceservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class DeviceInfoSimpleRequest(
    @field:NotBlank(message = "Name parameter is required")
    @JsonProperty("name")
    private val _name: String?,

    @field:NotNull(message = "Home id is required")
    @JsonProperty("homeId")
    private val _homeId: Int?,

    val roomId: Int?
) {
    val name: String
        get() = _name!!

    val homeId: Int
        get() = _homeId!!
}
