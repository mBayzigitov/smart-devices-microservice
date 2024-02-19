package ru.handh.deviceservice.dto.command

import ru.handh.deviceservice.enum.CapabilityCode

class SwitchLedCommand(
    code: CapabilityCode,
    override val value: Boolean
) : Command(code)