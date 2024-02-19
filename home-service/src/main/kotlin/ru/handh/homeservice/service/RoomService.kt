package ru.handh.homeservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.handh.homeservice.dto.Room
import ru.handh.homeservice.dto.message.RoomDeletionMessage
import ru.handh.homeservice.dto.request.RoomRequest
import ru.handh.homeservice.entity.OutboxMessageEntity
import ru.handh.homeservice.entity.RoomEntity
import ru.handh.homeservice.error.ApiError
import ru.handh.homeservice.repository.OutboxMessageRepository
import ru.handh.homeservice.repository.RoomRepository

@Service
class RoomService(
    private val roomRepository: RoomRepository,
    private val outboxMessageRepository: OutboxMessageRepository,
    private val homeService: HomeService,
    @Value("\${topic.home-service.deletion}")
    private val homeServiceDeletionTopicName: String,
    private val mapper: ObjectMapper
) {

    @Transactional
    fun save(homeId: Int,
             request: RoomRequest): Room {
        homeService.getHomeById(homeId)

        val roomEntity = request.toEntity(homeId)
        return roomRepository.save(roomEntity).toDto()
    }

    @Transactional
    fun edit(roomId: Int,
             request: RoomRequest): Room {
        val foundRoom = roomRepository.findByIdOrNull(roomId)
            ?: throw ApiError.ROOM_NOT_FOUND.toException()

        val editedRoom = request.toEntity(foundRoom.homeId, roomId)
        return roomRepository.save(editedRoom).toDto()
    }

    @Transactional
    fun remove(roomId: Int) =
        roomRepository.findByIdOrNull(roomId)?.let {
            val message = RoomDeletionMessage(roomId)
            outboxMessageRepository.save(message.toEntity())
            roomRepository.delete(it)
        } ?: throw ApiError.ROOM_NOT_FOUND.toException()


    @Transactional(readOnly = true)
    fun getRoomRelations(id: Int) =
        roomRepository.getRoomRelationsByRoomId(id)
            ?: throw ApiError.ROOM_NOT_FOUND.toException()

    private fun RoomRequest.toEntity(homeId: Int,
                                     id: Int = -1) =
        RoomEntity(
            id = id,
            homeId = homeId,
            name = name
        )

    private fun RoomDeletionMessage.toEntity(id: Int = -1) =
        OutboxMessageEntity(
            id = id,
            topic = homeServiceDeletionTopicName,
            data = this.toJsonData()
        )

    private fun RoomDeletionMessage.toJsonData() =
        mapper.writeValueAsString(this)

}