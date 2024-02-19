package ru.handh.apigateway.dto

import ru.handh.deviceservice.enum.DeviceCategory

data class DeviceSimple(
    val id: Int,
    val name: String,
    val category: DeviceCategory
)