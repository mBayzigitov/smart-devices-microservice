package ru.handh.deviceservice.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class DeviceInfoRequest(
    @field:NotBlank(message = "Tuya device ID is required")
    @JsonProperty("tuyaDeviceId")
    private val _tuyaDeviceId: String?,

    val name: String?,

    @field:NotNull(message = "Home id is required")
    @JsonProperty("homeId")
    private val _homeId: Int?,

    val roomId: Int?,
) {

    val tuyaDeviceId: String
        get() = _tuyaDeviceId!!

    val homeId: Int
        get() = _homeId!!

}