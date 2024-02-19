package ru.handh.deviceservice.converter

import org.springframework.stereotype.Component
import ru.handh.deviceservice.converter.dictionary.toTuyaCode
import ru.handh.deviceservice.dto.command.ColorCommand
import ru.handh.deviceservice.dto.tuya.TuyaCommand
import ru.handh.deviceservice.enum.CapabilityCode
import ru.handh.deviceservice.error.ApiError

@Component
class ColorTuyaConverter : TuyaConverter<ColorCommand> {

    override fun convert(data: ColorCommand): TuyaCommand =
        data.run {
            TuyaCommand(
                code = code.toTuyaCode() ?: throw ApiError.UNSUPPORTED_COMMAND_ERROR.toException(),
                value = value
            )
        }

    override val code: CapabilityCode = CapabilityCode.COLOR

}