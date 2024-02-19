package ru.handh.apigateway.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.handh.apigateway.dto.Home
import ru.handh.apigateway.dto.HomeSimple
import ru.handh.apigateway.dto.UserHomeDto
import ru.handh.apigateway.dto.UserIdDto
import ru.handh.apigateway.error.ApiError

@Component
class HomeRequestsClient(
    private val restTemplate: RestTemplate,

    @Value("\${homeservice.homes.api.path}")
    private val homeServiceBaseUrl: String
) {

    fun createHome(request: UserHomeDto) =
        restTemplate.postForObject(
            homeServiceBaseUrl,
            request,
            Home::class.java
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun editHome(request: UserHomeDto,
                 homeId: Int) =
        restTemplate.exchange(
            "${homeServiceBaseUrl}/{homeId}",
            HttpMethod.PUT,
            HttpEntity(request),
            Home::class.java,
            homeId
        ).body ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun getHomeInfo(homeId: Int) =
        restTemplate.getForObject(
            "${homeServiceBaseUrl}/{homeId}",
            Home::class.java,
            homeId
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun getAllHomes(userId: Int) =
        restTemplate.exchange(
            "${homeServiceBaseUrl}/all/{userId}",
            HttpMethod.GET,
            null,
            Array<HomeSimple>::class.java,
            mapOf("userId" to userId)
        ).body?.toList() ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun deleteHome(homeId: Int) =
        restTemplate.delete(
            "${homeServiceBaseUrl}/{homeId}",
            homeId
        )

    fun getOwnerId(homeId: Int) = restTemplate.getForObject(
        "$homeServiceBaseUrl/{id}/userId",
        UserIdDto::class.java,
        homeId
    ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

}