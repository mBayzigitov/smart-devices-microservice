package ru.handh.userservice.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.handh.userservice.dto.request.AuthDto
import ru.handh.userservice.dto.request.DeleteAccountDto
import ru.handh.userservice.dto.request.RefreshDto
import ru.handh.userservice.dto.request.RegisterDto
import ru.handh.userservice.dto.request.SignoutDto
import ru.handh.userservice.service.RefreshTokenService
import ru.handh.userservice.service.UserService

@RestController
@RequestMapping("/api")
class AuthController(
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService
) {

    @PostMapping("/register")
    fun registerUser(@Validated @RequestBody regRequest: RegisterDto) =
        userService.register(regRequest)

    @PostMapping("/auth")
    fun authenticateUser(@Validated @RequestBody authRequest: AuthDto) =
        userService.authenticate(authRequest)

    @DeleteMapping("/account")
    fun deleteAccount(@Validated @RequestBody deleteAccountDto: DeleteAccountDto) =
        userService.deleteAccount(deleteAccountDto)

    @PostMapping("/refresh")
    fun refreshToken(@Validated @RequestBody refreshRequest: RefreshDto) =
        refreshTokenService.refreshAccessToken(refreshRequest)

    @PostMapping("/signout")
    fun signOut(@Validated @RequestBody signoutRequest: SignoutDto) =
        refreshTokenService.signOut(signoutRequest)

}