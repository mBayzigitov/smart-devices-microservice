package ru.handh.deviceservice.dto

import ru.handh.deviceservice.enum.DeviceCategory

data class DeviceSimple(
    val id: Int,
    val name: String,
    val category: DeviceCategory
)