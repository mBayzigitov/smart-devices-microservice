package ru.handh.homeservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import ru.handh.homeservice.dto.HomeRoomDto
import ru.handh.homeservice.dto.Room
import ru.handh.homeservice.dto.message.RoomDeletionMessage
import ru.handh.homeservice.error.ApiError
import ru.handh.homeservice.repository.HomeRepository
import ru.handh.homeservice.repository.OutboxMessageRepository
import ru.handh.homeservice.repository.RoomRepository
import ru.handh.homeservice.service.container.PostgresTestContainer

@Import(RoomService::class,
        HomeService::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [],
    initializers = [PostgresTestContainer.Initializer::class]
)
@ExtendWith(SpringExtension::class)
class RoomServiceTest {

    @Autowired
    private lateinit var homeService: HomeService

    @SpykBean
    @Autowired
    private lateinit var roomRepository: RoomRepository

    @SpykBean
    @Autowired
    private lateinit var outboxMessageRepository: OutboxMessageRepository

    @Autowired
    private lateinit var roomService: RoomService

    @SpykBean
    @Autowired
    private lateinit var homeRepository: HomeRepository

    @MockkBean
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setup() {
        resetSequences(jdbcTemplate)
    }

    @Nested
    inner class SaveRoom {

        @Test
        fun `should save room successfully`() {
            val homeId = 1

            homeService.save(testHome)

            val savedRoom = roomService.save(homeId, roomRequest)
            verify(exactly = 1) { roomRepository.save(any()) }

            Assertions.assertNotNull(savedRoom)
            assertEquals(roomRequest.name, savedRoom.name)
        }

        @Test
        fun `should throw HOME_NOT_FOUND error`() {
            val homeId = 1

            expectApiException(ApiError.HOME_NOT_FOUND) {
                roomService.save(homeId, roomRequest)
                verify(exactly = 1) { homeService.getHomeById(any()) }
            }

        }

    }

    @Nested
    inner class EditRoom {

        @Test
        fun `should edit room successfully`() {
            val homeId = 1
            val roomId = 1

            homeRepository.save(
                testHome.toEntity()
            )
            roomRepository.save(
                roomRequest.toEntity(homeId)
            )

            val editedRoom = roomRequest.copy(
                _name = "Edited Room"
            )

            val expectedRoom = Room(
                id = roomId,
                name = editedRoom.name
            )

            val result = roomService.edit(roomId, editedRoom)
            assertEquals(expectedRoom, result)
        }

        @Test
        fun `should throw ROOM_NOT_FOUND error`() {
            val homeId = 1
            val roomId = 2

            homeRepository.save(
                testHome.toEntity()
            )
            roomRepository.save(
                roomRequest.toEntity(homeId)
            )

            expectApiException(ApiError.ROOM_NOT_FOUND) {
                roomService.edit(roomId, roomRequest)
            }
        }

    }

    @Nested
    inner class GetRoomRelationsByRoomId {

        @Test
        fun `should get relations successfully`() {
            val homeId = 1
            val roomId = 1
            val userId = 1

            homeRepository.save(
                testHome.toEntity()
            )
            roomRepository.save(
                roomRequest.toEntity(homeId)
            )

            val expectedRelations = HomeRoomDto(
                homeId = homeId,
                roomId = roomId,
                userId = userId
            )

            val result = roomService.getRoomRelations(roomId)
            verify { roomRepository.getRoomRelationsByRoomId(any()) }

            assertEquals(expectedRelations, result)
        }

        @Test
        fun `should throw ROOM_NOT_FOUND error`() {
            val homeId = 1
            val roomId = 2

            homeRepository.save(
                testHome.toEntity()
            )
            roomRepository.save(
                roomRequest.toEntity(homeId)
            )

            expectApiException(ApiError.ROOM_NOT_FOUND) {
                roomService.getRoomRelations(roomId)
                verify { roomRepository.getRoomRelationsByRoomId(any()) }
            }
        }

    }

    @Nested
    inner class RemoveRoom {

        @Test
        fun `should remove room successfully`() {
            val homeId = 1
            val roomId = 1
            val messageId = 1

            homeRepository.save(
                testHome.toEntity()
            )
            roomRepository.save(
                roomRequest.toEntity(homeId)
            )

            val message = RoomDeletionMessage(1)
            every { objectMapper.writeValueAsString(any()) } returns message.toJsonData()

            roomService.remove(roomId)

            verify(exactly = 1) { outboxMessageRepository.save(any()) }
            verify(exactly = 1) { roomRepository.delete(any()) }

            val deletionResult = roomRepository.findAll()

            val savedMessage = outboxMessageRepository.findById(messageId).get()
            val dataString = savedMessage.fromJsonString() as RoomDeletionMessage
            assertEquals(message.deletedRoomId, dataString.deletedRoomId)

            assertEquals(0, deletionResult.size)
        }

        @Test
        fun `should throw ROOM_NOT_FOUND_ERROR`() {
            val roomId = 1

            expectApiException(ApiError.ROOM_NOT_FOUND) {
                roomService.remove(roomId)
            }
        }

    }

}