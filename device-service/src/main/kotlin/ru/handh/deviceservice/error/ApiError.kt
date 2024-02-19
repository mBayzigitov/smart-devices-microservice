package ru.handh.deviceservice.error

import org.springframework.http.HttpStatus
import java.text.SimpleDateFormat
import java.util.Date

const val ERROR_DATE_PATTERN = "dd.M.yyyy hh:mm:ss"

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String
) {
    DEVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "Tuya device with specified ID not found"),
    DEVICE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Device with specified id already exists"),
    UNSUPPORTED_COMMAND_ERROR(HttpStatus.BAD_REQUEST, "Unsupported command indicated")
    ;

    fun toException() = ApiException(
        httpStatus = httpStatus,
        code = name,
        timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date()),
        message = message
    )

}