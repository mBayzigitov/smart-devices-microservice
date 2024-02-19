package ru.handh.apigateway.dto.request

data class DeviceInfoRequest(
    val tuyaDeviceId: String?,
    val name: String?,
    val homeId: Int?,
    val roomId: Int?
)