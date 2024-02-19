package ru.handh.apigateway.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.handh.apigateway.dto.request.RoomRequest
import ru.handh.apigateway.service.RoomRequestsService

@RestController
@RequestMapping("/api/rooms")
class RoomRequestsController(
    private val roomRequestsService: RoomRequestsService
) {

    @PostMapping
    fun createRoom(@RequestHeader("X-Access-Token") accessToken: String,
                   @RequestParam("homeId") homeId: Int,
                   @RequestBody request: RoomRequest) =
        roomRequestsService.createRoom(
            homeId = homeId,
            request = request
        )

    @PutMapping("/{roomId}")
    fun editRoom(@RequestHeader("X-Access-Token") accessToken: String,
                 @PathVariable roomId: Int,
                 @RequestBody request: RoomRequest) =
        roomRequestsService.editRoom(
            roomId = roomId,
            request = request
        )

    @DeleteMapping("/{roomId}")
    fun deleteRoom(@RequestHeader("X-Access-Token") accessToken: String,
                   @PathVariable roomId: Int) =
        roomRequestsService.deleteRoom(roomId)

}