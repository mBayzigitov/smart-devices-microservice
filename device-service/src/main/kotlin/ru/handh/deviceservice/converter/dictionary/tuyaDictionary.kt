package ru.handh.deviceservice.converter.dictionary

import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import ru.handh.deviceservice.enum.CapabilityCode
import ru.handh.deviceservice.error.ApiError

private val tuyaDictionary: BiMap<CapabilityCode, String> =
    ImmutableBiMap.Builder<CapabilityCode, String>()
        .put(CapabilityCode.SWITCH_LED, "switch_led")
        .put(CapabilityCode.TEMPERATURE, "temp_value_v2")
        .put(CapabilityCode.COLOR, "colour_data_v2")
        .put(CapabilityCode.BRIGHTNESS, "bright_value_v2")
        .build();

fun CapabilityCode.toTuyaCode(): String? =
    tuyaDictionary[this]

fun String.fromTuyaCode() =
    tuyaDictionary.inverse()[this]?.value
        ?: throw ApiError.UNSUPPORTED_COMMAND_ERROR.toException()
