package ru.handh.homeservice.config

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.handh.homeservice.dto.ErrorDto
import ru.handh.homeservice.error.ApiException
import ru.handh.homeservice.error.ERROR_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.Date

@ControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun handleException(exc: Exception): ResponseEntity<ErrorDto> {
        exc.printStackTrace()
        log.debug(exc) { exc.message }
        val errorDto = ErrorDto(
            code = "INTERNAL_API_EXCEPTION",
            message = "Internal server error",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(exc: ApiException): ResponseEntity<ErrorDto> {
        log.debug(exc) { exc.message }
        return ResponseEntity.status(exc.httpStatus).body(
            ErrorDto(code = exc.code, message = exc.message, timestamp = exc.timestamp)
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exc: MethodArgumentNotValidException): ResponseEntity<ErrorDto> {
        log.debug(exc) { exc.message }
        val errorDto = ErrorDto(
            message = exc.allErrors.first().defaultMessage!!,
            code = "ARGUMENT_NOT_VALID_ERROR",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

}