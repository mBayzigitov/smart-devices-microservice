package ru.handh.homeservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.handh.homeservice.dto.Home
import ru.handh.homeservice.dto.message.HomeDeletionMessage
import ru.handh.homeservice.dto.request.HomeRequest
import ru.handh.homeservice.entity.HomeEntity
import ru.handh.homeservice.entity.OutboxMessageEntity
import ru.handh.homeservice.error.ApiError
import ru.handh.homeservice.repository.HomeRepository
import ru.handh.homeservice.repository.OutboxMessageRepository

@Service
class HomeService(
    private val homeRepository: HomeRepository,
    private val outboxMessageRepository: OutboxMessageRepository,
    @Value("\${topic.home-service.deletion}")
    private val homeServiceDeletionTopicName: String,
    private val mapper: ObjectMapper
) {

    @Transactional
    fun save(request: HomeRequest) =
        request.toEntity()
            .let { homeRepository.save(it) }
            .toDto()

    @Transactional
    fun edit(id: Int,
             request: HomeRequest): Home {
        val foundHome = homeRepository.findByIdOrNull(id)
            ?: throw ApiError.HOME_NOT_FOUND.toException()

        val editedHome = foundHome.copy(
            name = request.name,
            address = request.address
        )

        return homeRepository.save(editedHome).toDto()
    }

    @Transactional(readOnly = true)
    fun getHomeById(id: Int) =
        homeRepository.findHomeEntityById(id)?.toDto()
            ?: throw ApiError.HOME_NOT_FOUND.toException()

    @Transactional
    fun remove(id: Int) {
        val foundHome = homeRepository.findHomeEntityById(id)
            ?: throw ApiError.HOME_NOT_FOUND.toException()

        homeRepository.delete(foundHome)

        val deletionMessage = HomeDeletionMessage(id)

        outboxMessageRepository.save(deletionMessage.toEntity())
    }

    @Transactional
    fun deleteHomesByUserId(userId: Int) {

        val foundHomes = homeRepository.findAllByUserId(userId)

        foundHomes.map { HomeDeletionMessage(it.id).toEntity() }
            .forEach { outboxMessageRepository.save(it) }

        homeRepository.deleteAllByUserId(userId)

    }

    @Transactional(readOnly = true)
    fun getUserIdByHomeId(id: Int) =
        homeRepository.findHomeEntityById(id)?.toUserIdDto()
            ?: throw ApiError.HOME_NOT_FOUND.toException()

    @Transactional(readOnly = true)
    fun getListOfHomes(userId: Int) =
        homeRepository.findAllByUserId(userId)
            .map { it.toSimpleDto() }

    private fun HomeRequest.toEntity(id: Int = -1) =
        HomeEntity(
            id = id,
            name = name,
            address = address,
            userId = userId
        )

    private fun HomeDeletionMessage.toEntity(id: Int = -1) =
        OutboxMessageEntity(
            id = id,
            topic = homeServiceDeletionTopicName,
            data = this.toJsonData()
        )

    private fun HomeDeletionMessage.toJsonData() =
        mapper.writeValueAsString(this)

}