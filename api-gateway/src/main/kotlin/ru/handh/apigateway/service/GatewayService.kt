package ru.handh.apigateway.service

import org.springframework.stereotype.Service
import ru.handh.apigateway.client.DeviceRequestsClient
import ru.handh.apigateway.client.HomeRequestsClient
import ru.handh.apigateway.client.RoomRequestsClient
import ru.handh.apigateway.error.ApiError

@Service
class GatewayService(
    private val homeRequestsClient: HomeRequestsClient,
    private val roomRequestsClient: RoomRequestsClient,
    private val deviceRequestsClient: DeviceRequestsClient
) {

    fun checkHomeOwner(homeId: Int) {
        val userId = RequestContext.get()

        val ownerId =
            homeRequestsClient.getOwnerId(homeId).userId

        if (ownerId != userId) {
            throw ApiError.USER_NOT_HOME_OWNER_ERROR.toException()
        }
    }

    fun checkRoomOwner(roomId: Int) {
        val userId = RequestContext.get()

        val ownerId =
            roomRequestsClient.getRoomRelations(roomId).userId

        if (ownerId != userId) {
            throw ApiError.USER_NOT_ROOM_OWNER_ERROR.toException()
        }
    }

    fun checkDeviceRelation(homeId: Int?,
                            roomId: Int?) {
        homeId ?: throw ApiError.HOME_NOT_FOUND.toException()

        roomId?.let {
            val actualHomeId =
                roomRequestsClient.getRoomRelations(it)
                    .homeId

            if (actualHomeId != homeId) {
                throw ApiError.ROOM_RELATION_ERROR.toException()
            }

            checkRoomOwner(it)
        } ?: checkHomeOwner(homeId)
    }

    fun checkDeviceOwner(tuyaDeviceId: String) {
        val deviceHomeId =
            deviceRequestsClient.getDeviceRelation(tuyaDeviceId).homeId

        val userId = RequestContext.get()

        val ownerId =
            homeRequestsClient.getOwnerId(deviceHomeId).userId

        if (ownerId != userId) {
            throw ApiError.USER_NOT_DEVICE_OWNER_ERROR.toException()
        }
    }

}