package ru.handh.homeservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
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
import ru.handh.homeservice.dto.Home
import ru.handh.homeservice.dto.HomeSimple
import ru.handh.homeservice.dto.UserIdDto
import ru.handh.homeservice.dto.message.HomeDeletionMessage
import ru.handh.homeservice.error.ApiError
import ru.handh.homeservice.repository.HomeRepository
import ru.handh.homeservice.repository.OutboxMessageRepository
import ru.handh.homeservice.service.container.PostgresTestContainer


@Import(HomeService::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [],
    initializers = [PostgresTestContainer.Initializer::class]
)
@ExtendWith(SpringExtension::class)
class HomeServiceTest {

    @SpykBean
    @Autowired
    private lateinit var homeRepository: HomeRepository

    @SpykBean
    @Autowired
    private lateinit var outboxMessageRepository: OutboxMessageRepository

    @Autowired
    private lateinit var homeService: HomeService

    @MockkBean
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setup() {
        resetSequences(jdbcTemplate)
    }

    @Nested
    inner class CreateHome {

        @Test
        fun `should save home successfully`() {
            val savedHome = homeService.save(request)
            verify(exactly = 1) { homeRepository.save(any()) }

            assertEquals(expectedHome, savedHome)
        }

    }

    @Nested
    inner class EditHome {

        @Test
        fun `should edit home successfully`() {
            val homeId = 1

            homeRepository.save(request.toEntity())

            val editedRequest = request.copy(_name = "Edited Name")

            val expectedHome = Home(
                id = 1,
                name = "Edited Name",
                address = "Sample Address",
                rooms = emptyList()
            )

            val result = homeService.edit(homeId, editedRequest)

            assertEquals(result, expectedHome)
        }

        @Test
        fun `should throw HOME_NOT_FOUND error`() {
            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.edit(1, request)
            }
        }

    }

    @Nested
    inner class GetHome {

        @Test
        fun `should get home successfully`() {
            val homeId = 1

            homeRepository.save(request.toEntity())
            verify(exactly = 1) { homeRepository.findHomeEntityById(homeId) }

            val result = homeService.getHomeById(homeId)

            assertEquals(expectedHome, result)
        }

        @Test
        fun `should throw HOME_NOT_FOUND error`() {
            val homeId = 1

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.getHomeById(homeId)
            }

            verify(exactly = 1) { homeRepository.findHomeEntityById(homeId) }
        }

    }

    @Nested
    inner class RemoveHome {

        @Test
        fun `should remove home successfully`() {
            val homeId = 1
            val messageId = 1

            homeRepository.save(request.toEntity())

            val message = HomeDeletionMessage(homeId)
            every { objectMapper.writeValueAsString(message) } returns message.toJsonData()

            homeService.remove(homeId)

            verify(exactly = 1) { outboxMessageRepository.save(any()) }

            val savedMessage = outboxMessageRepository.findById(messageId).get()
            val dataString = savedMessage.fromJsonString() as HomeDeletionMessage
            assertEquals(message.deletedHomeId, dataString.deletedHomeId)

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.getHomeById(homeId)
            }
        }

        @Test
        fun `should throw HOME_NOT_FOUND error`() {
            val homeId = 1

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.remove(homeId)
            }
        }

    }

    @Nested
    inner class GetHomesByUserId() {

        @Test
        fun `should return expected list`() {
            val userId = 1
            val differentUserId = 2

            insertMultipleEntities(
                repo = homeRepository,
                homeRequest = request,
                times = 5
            )

            insertMultipleEntities(
                repo = homeRepository,
                homeRequest = request.copy(_userId = differentUserId),
                times = 2
            )

            var initialId = 1
            val resultList = List(5) {
                HomeSimple(id = initialId++, name = "Sample Home")
            }

            val insertionResult = homeService.getListOfHomes(userId)
            assertEquals(insertionResult, resultList)
            verify(exactly = 1) { homeRepository.findAllByUserId(userId) }
        }

    }

    @Nested
    inner class DeleteHomesByUserId {

        @Test
        fun `should return empty list`() {
            val userId = 1

            insertMultipleEntities(
                repo = homeRepository,
                homeRequest = request,
                times = 5
            )

            val message = HomeDeletionMessage(1)
            every { objectMapper.writeValueAsString(any()) } returns message.toJsonData()

            var initialId = 1
            val resultList = List(5) {
                HomeSimple(id = initialId++, name = "Sample Home")
            }

            val insertionResult = homeService.getListOfHomes(userId)
            assertEquals(insertionResult, resultList)

            homeService.deleteHomesByUserId(userId)

            val expectedMessagesList = outboxMessageRepository.findAll()

            val deletionResult = homeService.getListOfHomes(userId)
            assertEquals(deletionResult, emptyList<HomeSimple>())

            assertEquals(5, expectedMessagesList.size)
        }

        @Test
        fun `should return homes with different userId`() {
            val userId = 1
            val differentUserId = 2

            insertMultipleEntities(
                repo = homeRepository,
                homeRequest = request,
                times = 5
            )

            insertMultipleEntities(
                repo = homeRepository,
                homeRequest = request.copy(_userId = differentUserId),
                times = 2
            )

            var initialId = 1
            val resultList = List(5) {
                HomeSimple(id = initialId++, name = "Sample Home")
            }

            val insertionResult = homeService.getListOfHomes(userId)
            assertEquals(resultList, insertionResult)

            val message = HomeDeletionMessage(1)
            every { objectMapper.writeValueAsString(any()) } returns message.toJsonData()

            homeService.deleteHomesByUserId(userId)

            val expectedMessagesList = outboxMessageRepository.findAll()

            var differentUserInitialId = 6
            val differentUserIdList = List(2) {
                HomeSimple(id = differentUserInitialId++, name = "Sample Home")
            }

            val deletionResult = homeService.getListOfHomes(userId)
            assertEquals(deletionResult, emptyList<HomeSimple>())

            val differentIdHomes = homeService.getListOfHomes(differentUserId)
            assertEquals(differentIdHomes, differentUserIdList)

            assertEquals(5, expectedMessagesList.size)
        }

    }

    @Nested
    inner class GetUserIdByHomeId {

        @Test
        fun `should get userId successfully`() {
            homeRepository.save(request.toEntity())

            val expectedReturn = UserIdDto(1)

            val result = homeService.getUserIdByHomeId(1)
            assertEquals(expectedReturn, result)
        }

        @Test
        fun `should throw HOME_NOT_FOUND error`() {
            val homeId = 1

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.getUserIdByHomeId(homeId)
            }
        }

    }

}