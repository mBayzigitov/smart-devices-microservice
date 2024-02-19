package ru.handh.apigateway.config

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import ru.handh.apigateway.dto.ErrorDto
import ru.handh.apigateway.error.ApiError
import ru.handh.apigateway.error.ApiException
import ru.handh.apigateway.error.ERROR_DATE_PATTERN
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

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleExternalServiceException(exc: HttpClientErrorException) =
        ResponseEntity.status(exc.rawStatusCode)
            .contentType(MediaType.APPLICATION_JSON)
            .body(exc.responseBodyAsString)

    @ExceptionHandler(RestClientException::class)
    fun handleRestClientException(exc: RestClientException): ResponseEntity<ErrorDto> {
        val err = ApiError.SERVICE_UNAVAILABLE

        val errorDto = ErrorDto(
            code = err.name,
            message = err.message,
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

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingHeaderException(exc: MissingRequestHeaderException): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            message = "X-Access-Token header is required",
            code = "INVALID_CREDENTIALS_ERROR",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJsonException(exc: HttpMessageNotReadableException): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            message = "Invalid request body received",
            code = "INVALID_REQUEST_BODY_ERROR",
            timestamp = SimpleDateFormat(ERROR_DATE_PATTERN).format(Date())
        )
        return ResponseEntity.badRequest().body(errorDto)
    }

}