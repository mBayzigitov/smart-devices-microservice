package ru.handh.apigateway.dto.command

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ru.handh.apigateway.enum.CapabilityCode
import ru.handh.apigateway.util.BRIGHTNESS
import ru.handh.apigateway.util.COLOR
import ru.handh.apigateway.util.SWITCH_LED
import ru.handh.apigateway.util.TEMPERATURE

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