package ru.handh.deviceservice.converter

import ru.handh.deviceservice.dto.command.Command
import ru.handh.deviceservice.dto.tuya.TuyaCommand
import ru.handh.deviceservice.enum.CapabilityCode

interface TuyaConverter<I : Command> {
    val code: CapabilityCode

    fun convert(data: I): TuyaCommand
}