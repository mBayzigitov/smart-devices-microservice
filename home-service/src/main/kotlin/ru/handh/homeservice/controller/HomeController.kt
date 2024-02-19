package ru.handh.homeservice.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.handh.homeservice.dto.HomeSimple
import ru.handh.homeservice.dto.request.HomeRequest
import ru.handh.homeservice.service.HomeService

@RestController
@RequestMapping("/api/homes")
class HomeController(
    private val service: HomeService
) {

    @PostMapping
    fun createHome(@Validated @RequestBody request: HomeRequest) =
        service.save(request)

    @PutMapping("/{id}")
    fun editHome(@PathVariable id: Int,
                 @Validated @RequestBody request: HomeRequest) =
        service.edit(id, request)

    @GetMapping("/{id}")
    fun getHome(@PathVariable id: Int) =
        service.getHomeById(id)

    @DeleteMapping("/{id}")
    fun deleteHome(@PathVariable id: Int) =
        service.remove(id)

    @GetMapping("/all/{userId}")
    fun getListOfHomes(@PathVariable("userId") userId: Int): List<HomeSimple> =
        service.getListOfHomes(userId)

    @GetMapping("/{id}/userId")
    fun getUserIdByHomeId(@PathVariable id: Int) =
        service.getUserIdByHomeId(id)

}