package ru.handh.apigateway.dto.command

import ru.handh.apigateway.dto.ColorHsvDto
import ru.handh.apigateway.enum.CapabilityCode

class ColorCommand(
    code: CapabilityCode,
    override val value: ColorHsvDto
) : Command(code)