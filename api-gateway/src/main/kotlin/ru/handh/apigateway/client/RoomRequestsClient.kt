package ru.handh.apigateway.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.handh.apigateway.dto.HomeRoomDto
import ru.handh.apigateway.dto.Room
import ru.handh.apigateway.dto.request.RoomRequest
import ru.handh.apigateway.error.ApiError

@Component
class RoomRequestsClient(
    private val restTemplate: RestTemplate,

    @Value("\${homeservice.rooms.api.path}")
    private val homeServiceBaseUrl: String
) {

    fun createRoom(homeId: Int,
                   request: RoomRequest) =
        restTemplate.postForObject(
            "${homeServiceBaseUrl}?homeId=$homeId",
            request,
            Room::class.java
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun editRoom(roomId: Int,
                 request: RoomRequest) =
        restTemplate.exchange(
            "${homeServiceBaseUrl}/{roomId}",
            HttpMethod.PUT,
            HttpEntity(request),
            Room::class.java,
            roomId
        ).body ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun deleteRoom(roomId: Int) =
        restTemplate.delete(
            "${homeServiceBaseUrl}/{roomId}",
            roomId
        )

    fun getRoomRelations(homeId: Int) =
        restTemplate.getForObject(
            "$homeServiceBaseUrl/{id}/userId",
            HomeRoomDto::class.java,
            homeId
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

}