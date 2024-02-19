package ru.handh.deviceservice.dto.command

import ru.handh.deviceservice.enum.CapabilityCode

class TemperatureCommand(
    code: CapabilityCode,
    override val value: Int
) : Command(code)