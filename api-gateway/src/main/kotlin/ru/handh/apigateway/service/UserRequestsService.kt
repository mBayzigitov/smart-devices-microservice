package ru.handh.apigateway.service

import org.springframework.stereotype.Service
import ru.handh.apigateway.client.UserRequestsClient
import ru.handh.apigateway.dto.ManageAccountDto
import ru.handh.apigateway.dto.TokenPair
import ru.handh.apigateway.dto.TokensDto
import ru.handh.apigateway.dto.request.AccessTokenRequest
import ru.handh.apigateway.dto.request.AuthRequest
import ru.handh.apigateway.dto.request.DeleteAccountRequest
import ru.handh.apigateway.dto.request.RefreshTokenRequest
import ru.handh.apigateway.dto.request.RegisterRequest

@Service
class UserRequestsService(
    private val userRequestsClient: UserRequestsClient
) {

    fun registerUser(request: RegisterRequest) =
        userRequestsClient.registerUser(request)


    fun authUser(request: AuthRequest) =
        userRequestsClient.authorizeUser(request)

    fun deleteUser(accessToken: String,
                   request: DeleteAccountRequest) =
        userRequestsClient.deleteAccount(
            ManageAccountDto(
                accessToken = accessToken,
                password = request.password
            )
        )

    fun refreshUserToken(accessToken: String,
                         request: RefreshTokenRequest): TokenPair {
        val receivedTokenPair = TokensDto(
            accessToken = accessToken,
            refreshToken = request.refreshToken
        )

        return userRequestsClient.refreshUserToken(receivedTokenPair)
    }

    fun signOut(accessToken: String) =
        userRequestsClient.signOut(
            AccessTokenRequest(accessToken)
        )

}