package ru.handh.homeservice.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.handh.homeservice.dto.ErrorDto
import ru.handh.homeservice.dto.Home
import ru.handh.homeservice.dto.message.DeletionMessage
import ru.handh.homeservice.dto.request.HomeRequest
import ru.handh.homeservice.dto.request.RoomRequest
import ru.handh.homeservice.entity.HomeEntity
import ru.handh.homeservice.entity.OutboxMessageEntity
import ru.handh.homeservice.entity.RoomEntity
import ru.handh.homeservice.error.ApiError
import ru.handh.homeservice.error.ApiException
import ru.handh.homeservice.repository.HomeRepository
import java.nio.charset.StandardCharsets

private val mapper = jacksonObjectMapper()

val roomRequest = RoomRequest(_name = "Test Room")

val request = HomeRequest(
    _userId = 1,
    _name = "Sample Home",
    address = "Sample Address"
)

val expectedHome = Home(
    id = 1,
    name = request.name,
    address = request.address,
    rooms = emptyList()
)

val testHome = HomeRequest(
    _userId = 1,
    _name = "Test Home",
    address = null
)

fun HomeRequest.toEntity(id: Int = -1) =
    HomeEntity(
        id = id,
        name = name,
        address = address,
        userId = userId
    )

fun RoomRequest.toEntity(homeId: Int,
                         id: Int = -1) =
    RoomEntity(
        id = id,
        homeId = homeId,
        name = name
    )

fun insertMultipleEntities(repo: HomeRepository,
                           homeRequest: HomeRequest,
                           times: Int) {

    for (x in 1..times) {
        repo.save(homeRequest.toEntity())
    }

}

fun expectApiException(apiError: ApiError,
                       executable: () -> Unit) {
    val ex = assertThrows<ApiException> {
        executable()
    }
    assertEquals(apiError.name, ex.code)
}

fun ResultActions.andExpectValidationError() {
    val contentAsString = andReturn().response.getContentAsString(StandardCharsets.UTF_8)
    val ex = mapper.readValue(contentAsString, ErrorDto::class.java)
    println("${ex.code}; ${ex.message}")
    assertEquals("ARGUMENT_NOT_VALID_ERROR", ex.code)
    this.andExpect(MockMvcResultMatchers.status().isBadRequest)
}

fun ResultActions.andExpectHomeNotFoundError() {
    val contentAsString = andReturn().response.getContentAsString(StandardCharsets.UTF_8)
    val ex = mapper.readValue(contentAsString, ErrorDto::class.java)
    println("${ex.code}; ${ex.message}")
    assertEquals("HOME_NOT_FOUND_ERROR", ex.code)
    this.andExpect(MockMvcResultMatchers.status().isBadRequest)
}

fun OutboxMessageEntity.fromJsonString() =
    mapper.readValue(this.data, DeletionMessage::class.java)

fun DeletionMessage.toJsonData() =
    mapper.writeValueAsString(this)

fun resetSequences(jdbcTemplate: JdbcTemplate) {
    jdbcTemplate.execute("""
            alter sequence homes_id_seq restart;
            alter sequence rooms_id_seq restart;
            alter sequence outbox_messages_id_seq restart;
        """
    )
}