package ru.handh.deviceservice.dto.command

import ru.handh.deviceservice.enum.CapabilityCode

class BrightnessCommand (
    code: CapabilityCode,
    override val value: Int
) : Command(code)