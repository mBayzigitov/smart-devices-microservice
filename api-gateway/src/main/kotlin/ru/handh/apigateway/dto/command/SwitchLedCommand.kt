package ru.handh.apigateway.dto.command

import ru.handh.apigateway.enum.CapabilityCode

class SwitchLedCommand(
    code: CapabilityCode,
    override val value: Boolean
) : Command(code)