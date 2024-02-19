package ru.handh.apigateway.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.handh.apigateway.dto.request.HomeRequest
import ru.handh.apigateway.service.HomeRequestsService

@RestController
@RequestMapping("/api/homes")
class HomeRequestsController(
    private val homeRequestsService: HomeRequestsService
) {

    @PostMapping
    fun createHome(@RequestHeader("X-Access-Token") accessToken: String,
                   @RequestBody request: HomeRequest) =
        homeRequestsService.createHome(request)

    @PutMapping("/{homeId}")
    fun editHome(@RequestHeader("X-Access-Token") accessToken: String,
                 @RequestBody request: HomeRequest,
                 @PathVariable homeId: Int) =
        homeRequestsService.editHome(
            request = request,
            homeId = homeId
        )

    @GetMapping("/{homeId}")
    fun getHomeInfo(@RequestHeader("X-Access-Token") accessToken: String,
                    @PathVariable homeId: Int) =
        homeRequestsService.getHomeInfo(homeId)

    @GetMapping
    fun getAllHomes(@RequestHeader("X-Access-Token") accessToken: String) =
        homeRequestsService.getAllHomes()

    @DeleteMapping("/{homeId}")
    fun deleteHome(@RequestHeader("X-Access-Token") accessToken: String,
                   @PathVariable homeId: Int) =
        homeRequestsService.deleteHome(homeId)

}