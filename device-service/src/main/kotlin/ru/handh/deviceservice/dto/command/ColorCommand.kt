package ru.handh.deviceservice.dto.command

import ru.handh.deviceservice.enum.CapabilityCode
import ru.handh.deviceservice.dto.ColorHsvDto

class ColorCommand (
    code: CapabilityCode,
    override val value: ColorHsvDto
) : Command(code)