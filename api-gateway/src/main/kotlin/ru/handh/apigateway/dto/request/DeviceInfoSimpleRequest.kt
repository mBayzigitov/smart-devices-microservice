package ru.handh.apigateway.dto.request

data class DeviceInfoSimpleRequest(
    val name: String?,
    val homeId: Int?,
    val roomId: Int?
)
