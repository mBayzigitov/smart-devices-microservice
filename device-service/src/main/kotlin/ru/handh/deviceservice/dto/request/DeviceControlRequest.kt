package ru.handh.deviceservice.dto.request

import ru.handh.deviceservice.dto.command.Command

class DeviceControlRequest(
    val capabilities: List<Command>
)