package ru.handh.deviceservice.config

import com.tuya.connector.api.exceptions.ConnectorResultException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.handh.deviceservice.dto.ErrorDto
import ru.handh.deviceservice.error.ApiException
import ru.handh.deviceservice.error.ERROR_DATE_PATTERN
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

    @ExceptionHandler(ApiException::class)
    fun handleApiException(exc: ApiException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(exc.httpStatus).body(
            ErrorDto(code = exc.code, message = exc.message, timestamp = exc.timestamp)
        )
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class,
        IllegalStateException::class,
        ConnectorResultException::class])
    fun handleInvalidRequestException(exc: Exception): ResponseEntity<ErrorDto> {
        exc.printStackTrace()
        val errorDto = ErrorDto(
            message = "Invalid request received",
            code = "INVALID_REQUEST_ERROR",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exc: MethodArgumentNotValidException): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            message = exc.allErrors.first().defaultMessage!!,
            code = "ARGUMENT_NOT_VALID_ERROR",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

}