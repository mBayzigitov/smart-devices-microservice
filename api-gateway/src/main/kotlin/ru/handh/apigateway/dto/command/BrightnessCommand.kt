package ru.handh.apigateway.dto.command

import ru.handh.apigateway.enum.CapabilityCode

class BrightnessCommand(
    code: CapabilityCode,
    override val value: Int
) : Command(code)