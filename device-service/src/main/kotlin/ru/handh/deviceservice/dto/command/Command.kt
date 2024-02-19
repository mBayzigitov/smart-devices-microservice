package ru.handh.deviceservice.dto.command

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ru.handh.deviceservice.enum.CapabilityCode
import ru.handh.deviceservice.util.BRIGHTNESS
import ru.handh.deviceservice.util.COLOR
import ru.handh.deviceservice.util.SWITCH_LED
import ru.handh.deviceservice.util.TEMPERATURE

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "code",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = SwitchLedCommand::class,
        name = SWITCH_LED
    ),
    JsonSubTypes.Type(
        value = TemperatureCommand::class,
        name = TEMPERATURE
    ),
    JsonSubTypes.Type(
        value = BrightnessCommand::class,
        name = BRIGHTNESS
    ),
    JsonSubTypes.Type(
        value = ColorCommand::class,
        name = COLOR
    )
)
abstract class Command(
    val code: CapabilityCode,
) {
    abstract val value: Any?
}