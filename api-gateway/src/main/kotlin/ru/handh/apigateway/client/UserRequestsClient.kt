package ru.handh.apigateway.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.handh.apigateway.dto.ManageAccountDto
import ru.handh.apigateway.dto.TokenPair
import ru.handh.apigateway.dto.TokensDto
import ru.handh.apigateway.dto.request.AccessTokenRequest
import ru.handh.apigateway.dto.request.AuthRequest
import ru.handh.apigateway.dto.request.RegisterRequest
import ru.handh.apigateway.error.ApiError

@Component
class UserRequestsClient(
    private val restTemplate: RestTemplate,

    @Value("\${userservice.api.path}")
    private val userServiceBaseUrl: String
) {

    fun registerUser(request: RegisterRequest) =
        restTemplate.postForObject(
            "${userServiceBaseUrl}/register",
            request,
            TokenPair::class.java
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun authorizeUser(request: AuthRequest) =
        restTemplate.postForObject(
            "${userServiceBaseUrl}/auth",
            request, TokenPair::class.java
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun deleteAccount(request: ManageAccountDto) =
        restTemplate.exchange(
            "${userServiceBaseUrl}/account",
            HttpMethod.DELETE,
            HttpEntity(request),
            Unit::class.java
        )

    fun refreshUserToken(request: TokensDto) =
        restTemplate.postForObject(
            "${userServiceBaseUrl}/refresh",
            request, TokenPair::class.java
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun signOut(request: AccessTokenRequest) =
        restTemplate.postForLocation(
            "${userServiceBaseUrl}/signout",
            request
        )

}