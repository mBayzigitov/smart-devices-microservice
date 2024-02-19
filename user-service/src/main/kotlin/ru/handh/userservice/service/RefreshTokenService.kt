package ru.handh.userservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.handh.userservice.dto.JwtResponse
import ru.handh.userservice.dto.request.RefreshDto
import ru.handh.userservice.dto.request.SignoutDto
import ru.handh.userservice.entity.RefreshTokenEntity
import ru.handh.userservice.error.ApiError
import ru.handh.userservice.repository.RefreshTokenRepository
import java.time.LocalDateTime
import java.util.UUID

private const val refreshTokenTtl: Long = 86400 // 1 day

@Service
class RefreshTokenService(
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.tokenTtl}")
    private val accessTokenTtl: Long
) {

    fun generateJwtTokens(userId: Int, username: String) : JwtResponse {
        val accessTokenId = UUID.randomUUID() // create access token id
        val accessToken = jwtService.generateToken(
            id = accessTokenId, // store created uuid in payload
            userId = userId,
            username = username
        )

        val refreshToken = UUID.randomUUID() // create refresh token id
        val refreshTokenEntity = RefreshTokenEntity(
            id = refreshToken,
            accessTokenId = accessTokenId,
            expiresAt = LocalDateTime.now().plusSeconds(refreshTokenTtl)
        )
        saveRefreshToken(refreshTokenEntity)

        return JwtResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            ttl = accessTokenTtl
        )
    }

    fun signOut(signoutRequest: SignoutDto) {
        deactivateToken(signoutRequest.accessToken)
    }

    fun refreshAccessToken(refreshRequest: RefreshDto) : JwtResponse {
        val previousRefreshToken =
            refreshTokenRepository.findByIdOrNull(refreshRequest.refreshToken) ?:
            throw ApiError.TOKEN_NOT_FOUND_ERROR.toException()

        deleteToken(previousRefreshToken)

        return if (previousRefreshToken.expiresAt.isBefore(LocalDateTime.now())) {
            throw ApiError.REFRESH_TOKEN_EXPIRED.toException()
        } else {
            val tokenPayload = jwtService
                .parseTokenInfo(refreshRequest.accessToken)

            val userId = tokenPayload.userId
            val username = tokenPayload.username

            generateJwtTokens(
                userId = userId,
                username = username
            )
        }
    }

    fun deactivateToken(accessToken: String) {
        val accessTokenUUID = jwtService
            .parseTokenInfo(accessToken)
            .uuid
        val accessTokenId = UUID.fromString(accessTokenUUID)
        val refreshToken =
            refreshTokenRepository.findByAccessTokenId(accessTokenId) ?: throw ApiError.TOKEN_NOT_FOUND_ERROR.toException()

        deleteToken(refreshToken)
    }

    @Transactional
    fun saveRefreshToken(token: RefreshTokenEntity) =
        refreshTokenRepository.save(token)

    @Transactional
    fun deleteToken(token: RefreshTokenEntity) =
        refreshTokenRepository.delete(token)

}