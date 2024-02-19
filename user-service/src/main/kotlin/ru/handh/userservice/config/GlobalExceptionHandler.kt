package ru.handh.userservice.config

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.handh.userservice.dto.ErrorDto
import ru.handh.userservice.error.ApiException
import ru.handh.userservice.error.ERROR_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.Date

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(exc: Exception): ResponseEntity<ErrorDto> {
        exc.printStackTrace()
        val errorDto = ErrorDto(
            code = "INTERNAL_API_EXCEPTION",
            message = "Internal server error",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

    // --- Jwt.verify() exceptions handling ---
    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpiredException(exc: Exception): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            code = "ACCESS_TOKEN_EXPIRED",
            message = "Access token expired",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

    @ExceptionHandler(JWTVerificationException::class)
    fun handleInvalidAccessToken(exc: Exception): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            code = "INVALID_ACCESS_TOKEN",
            message = "Invalid access token received",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }
    // ----------------------------------------

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleUUIDException(exc: Exception): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            code = "UUID_FORMAT_ERROR",
            message = "Invalid UUID received",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(exc: ApiException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(exc.httpStatus).body(
            ErrorDto(code = exc.code, message = exc.message, timestamp = exc.timestamp)
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exc: MethodArgumentNotValidException): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            message = exc.allErrors.first().defaultMessage!!,
            code = "INVALID_CREDENTIALS_ERROR",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

}