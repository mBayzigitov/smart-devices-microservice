package ru.handh.apigateway.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.handh.apigateway.dto.request.AuthRequest
import ru.handh.apigateway.dto.request.DeleteAccountRequest
import ru.handh.apigateway.dto.request.RefreshTokenRequest
import ru.handh.apigateway.dto.request.RegisterRequest
import ru.handh.apigateway.service.UserRequestsService

@RestController
@RequestMapping("/api")
class UserRequestsController(
    private val userRequestsService: UserRequestsService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest) =
        userRequestsService.registerUser(request)

    @PostMapping("/auth")
    fun authUser(@RequestBody request: AuthRequest) =
        userRequestsService.authUser(request)

    @DeleteMapping("/account")
    fun deleteUser(@RequestHeader("X-Access-Token") accessToken: String,
                   @RequestBody request: DeleteAccountRequest) =
        userRequestsService.deleteUser(
            accessToken = accessToken,
            request = request
        )

    @PostMapping("/refresh")
    fun refreshUserToken(@RequestHeader("X-Access-Token") accessToken: String,
                         @RequestBody request: RefreshTokenRequest) =
        userRequestsService.refreshUserToken(
            accessToken = accessToken,
            request = request
        )

    @PostMapping("/signout")
    fun signOut(@RequestHeader("X-Access-Token") accessToken: String) =
        userRequestsService.signOut(accessToken)

}