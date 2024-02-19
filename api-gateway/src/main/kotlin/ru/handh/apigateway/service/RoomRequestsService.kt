package ru.handh.apigateway.service

import org.springframework.stereotype.Service
import ru.handh.apigateway.client.RoomRequestsClient
import ru.handh.apigateway.dto.Room
import ru.handh.apigateway.dto.request.RoomRequest

@Service
class RoomRequestsService(
    private val roomRequestsClient: RoomRequestsClient,
    private val gatewayService: GatewayService
) {

    fun createRoom(homeId: Int,
                   request: RoomRequest): Room {
        gatewayService.checkHomeOwner(homeId)

        return roomRequestsClient.createRoom(
            homeId = homeId,
            request = request
        )
    }

    fun editRoom(roomId: Int,
                 request: RoomRequest): Room {
        gatewayService.checkRoomOwner(roomId)

        return roomRequestsClient.editRoom(
            roomId = roomId,
            request = request
        )
    }

    fun deleteRoom(roomId: Int) {
        gatewayService.checkRoomOwner(roomId)

        roomRequestsClient.deleteRoom(roomId)
    }

}