package ru.handh.apigateway.dto

data class DeviceRelationDto(
    val tuyaDeviceId: String,
    val homeId: Int,
    val roomId: Int?
)
