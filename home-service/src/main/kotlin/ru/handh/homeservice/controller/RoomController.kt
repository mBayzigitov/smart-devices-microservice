package ru.handh.homeservice.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.handh.homeservice.dto.request.RoomRequest
import ru.handh.homeservice.service.RoomService

@RestController
@RequestMapping("/api/rooms")
class RoomController(
    private val service: RoomService
) {

    @PostMapping
    fun createRoom(@RequestParam("homeId") homeId: Int,
                   @Validated @RequestBody request: RoomRequest) =
        service.save(homeId, request)

    @PutMapping("/{roomId}")
    fun editRoom(@PathVariable roomId: Int,
                 @Validated @RequestBody request: RoomRequest) =
        service.edit(roomId, request)

    @DeleteMapping("/{roomId}")
    fun removeRoom(@PathVariable roomId: Int) =
        service.remove(roomId)

    @GetMapping("/{id}/userId")
    fun getRoomRelations(@PathVariable id: Int) =
        service.getRoomRelations(id)

}