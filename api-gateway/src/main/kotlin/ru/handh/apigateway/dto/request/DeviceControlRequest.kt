package ru.handh.apigateway.dto.request

import ru.handh.apigateway.dto.command.Command

class DeviceControlRequest(
    val capabilities: List<Command>
)