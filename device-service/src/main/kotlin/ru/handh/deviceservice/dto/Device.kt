package ru.handh.deviceservice.dto

import ru.handh.deviceservice.enum.DeviceCategory

data class Device(
    val id: Int,
    val name: String,
    val category: DeviceCategory,
    val capabilities: List<CapabilityDto>
)