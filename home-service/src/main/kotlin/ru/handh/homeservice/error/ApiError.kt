package ru.handh.homeservice.error

import org.springframework.http.HttpStatus
import java.text.SimpleDateFormat
import java.util.Date

const val ERROR_DATE_PATTERN = "dd.M.yyyy hh:mm:ss"

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String
) {
    HOME_NOT_FOUND(HttpStatus.NOT_FOUND, "No home with specified id was found"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "No room with specified id was found")
    ;

    fun toException() = ApiException(
        httpStatus = httpStatus,
        code = name,
        timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date()),
        message = message
    )

}