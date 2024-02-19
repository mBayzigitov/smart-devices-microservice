package ru.handh.apigateway.enum

import ru.handh.apigateway.util.BRIGHTNESS as BRIGHTNESS_VALUE
import ru.handh.apigateway.util.COLOR as COLOR_VALUE
import ru.handh.apigateway.util.SWITCH_LED as SWITCH_LED_VALUE
import ru.handh.apigateway.util.TEMPERATURE as TEMPERATURE_VALUE

enum class CapabilityCode(
    val value: String
) {
    SWITCH_LED(SWITCH_LED_VALUE),
    TEMPERATURE(TEMPERATURE_VALUE),
    COLOR(COLOR_VALUE),
    BRIGHTNESS(BRIGHTNESS_VALUE);
}

