package ru.handh.deviceservice.converter

import org.springframework.stereotype.Component
import ru.handh.deviceservice.converter.dictionary.toTuyaCode
import ru.handh.deviceservice.dto.command.TemperatureCommand
import ru.handh.deviceservice.dto.tuya.TuyaCommand
import ru.handh.deviceservice.enum.CapabilityCode
import ru.handh.deviceservice.error.ApiError

@Component
class TemperatureTuyaConverter : TuyaConverter<TemperatureCommand> {
    override fun convert(data: TemperatureCommand): TuyaCommand =
        data.run {
            TuyaCommand(
                code = code.toTuyaCode() ?: throw ApiError.UNSUPPORTED_COMMAND_ERROR.toException(),
                value = value
            )
        }

    override val code: CapabilityCode = CapabilityCode.TEMPERATURE
}