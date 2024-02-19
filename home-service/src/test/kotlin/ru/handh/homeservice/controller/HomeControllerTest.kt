package ru.handh.homeservice.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.handh.homeservice.config.GlobalExceptionHandler
import ru.handh.homeservice.dto.Home
import ru.handh.homeservice.dto.request.HomeRequest
import ru.handh.homeservice.service.HomeService
import ru.handh.homeservice.service.andExpectValidationError

@WebMvcTest
@Import(HomeController::class)
@ContextConfiguration(
    classes = [
        GlobalExceptionHandler::class,
    ]
)
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var homeService: HomeService

    private val mapper = jacksonObjectMapper()

    @Nested
    inner class CreateHome {

        @Test
        fun `should create home successfully`() {
            val userId = 1
            val name = "Sample Home"
            val address = "123 Main St"
            val request = HomeRequest(userId, name, address)
            val expectedHome = Home(id = 1, name = name, address = address, rooms = emptyList())

            every { homeService.save(any()) } returns expectedHome

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/homes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            ).andExpect(status().isOk)
        }

        @Test
        fun `should get validation error because of empty name`() {
            val userId = 1
            val request = HomeRequest(
                _userId = userId,
                _name = "",
                address = null
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/homes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            ).andExpectValidationError()
        }

        @Test
        fun `should get validation error because of name field size constraint`() {
            val userId = 1
            val request = HomeRequest(
                _userId = userId,
                _name = "s".repeat(100),
                address = null
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/homes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            ).andExpectValidationError()
        }


    }

    @Nested
    inner class EditHome {

        @Test
        fun `should edit home successfully`() {
            val homeId = 1
            val userId = 1
            val name = "Edited Home"
            val address = "456 Main St"
            val request = HomeRequest(userId, name, address)
            val expectedHome = Home(id = homeId, name = name, address = address, rooms = emptyList())

            every { homeService.edit(homeId, request) } returns expectedHome

            mockMvc.perform(
                put("/api/homes/{id}", homeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
            ).andExpect(status().isOk)

            verify { homeService.edit(homeId, request) }
        }

        @Test
        fun `should get validation error because of address field size constraint`() {
            val homeId = 1
            val request = HomeRequest(
                _userId = 1,
                _name = "Valid Name",
                address = "Invalid Address".repeat(300)
            )

            mockMvc.perform(
                put("/api/homes/{id}", homeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            ).andExpectValidationError()
        }

    }

}