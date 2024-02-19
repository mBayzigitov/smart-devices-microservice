package ru.handh.userservice.error

import org.springframework.http.HttpStatus
import java.text.SimpleDateFormat
import java.util.Date

const val ERROR_DATE_PATTERN = "dd.M.yyyy hh:mm:ss"

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String
) {
    REGISTRATION_ERROR(HttpStatus.CONFLICT, "User with specified username already exists"),
    PASSWORD_CONFIRMATION_ERROR(HttpStatus.BAD_REQUEST,"The password and its confirmation do not match"),
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Username not found"),
    TOKEN_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Specified token doesn't exist"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Specified refresh token has expired"),
    WRONG_PASSWORD_ERROR(HttpStatus.UNAUTHORIZED, "Incorrect password entered")
    ;

    fun toException() = ApiException(
        httpStatus = httpStatus,
        code = name,
        timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date()),
        message = message
    )

}