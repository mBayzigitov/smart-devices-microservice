package ru.handh.deviceservice.dto

data class DeviceRelationDto(
    val tuyaDeviceId: String,
    val homeId: Int,
    val roomId: Int?
)
