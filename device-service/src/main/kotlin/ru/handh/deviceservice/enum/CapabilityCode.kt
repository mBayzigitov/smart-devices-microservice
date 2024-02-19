package ru.handh.deviceservice.enum

import ru.handh.deviceservice.util.SWITCH_LED as SWITCH_LED_VALUE
import ru.handh.deviceservice.util.TEMPERATURE as TEMPERATURE_VALUE
import ru.handh.deviceservice.util.COLOR as COLOR_VALUE
import ru.handh.deviceservice.util.BRIGHTNESS as BRIGHTNESS_VALUE

enum class CapabilityCode(
    val value: String
) {
    SWITCH_LED(SWITCH_LED_VALUE),
    TEMPERATURE(TEMPERATURE_VALUE),
    COLOR(COLOR_VALUE),
    BRIGHTNESS(BRIGHTNESS_VALUE);
}

