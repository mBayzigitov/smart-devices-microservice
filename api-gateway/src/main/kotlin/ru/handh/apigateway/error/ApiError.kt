package ru.handh.apigateway.error

import org.springframework.http.HttpStatus
import java.text.SimpleDateFormat
import java.util.Date

const val ERROR_DATE_PATTERN = "dd.M.yyyy hh:mm:ss"

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String
) {
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "The required service is currently unavailable"),
    HOME_NOT_FOUND(HttpStatus.NOT_FOUND, "No home with specified id was found"),
    USER_NOT_HOME_OWNER_ERROR(HttpStatus.FORBIDDEN, "User is not an owner of specified home"),
    USER_NOT_ROOM_OWNER_ERROR(HttpStatus.FORBIDDEN, "User is not an owner of specified room"),
    USER_NOT_DEVICE_OWNER_ERROR(HttpStatus.FORBIDDEN, "User is not an owner of specified device"),
    ROOM_RELATION_ERROR(HttpStatus.BAD_REQUEST, "Specified room doesn't belong at specified home")
    ;

    fun toException() = ApiException(
        httpStatus = httpStatus,
        code = name,
        timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date()),
        message = message
    )

}