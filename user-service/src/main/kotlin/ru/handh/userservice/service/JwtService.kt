package ru.handh.userservice.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.handh.userservice.dto.TokenInfoDto
import java.time.Instant
import java.util.UUID

private const val CLAIM_USERNAME = "username"
private const val CLAIM_USER_ID = "userId"

@Service
class JwtService(
    @Value("\${jwt.tokenTtl}")
    private val tokenTtl: Long,

    @Value("\${jwt.secret}")
    private val secret: String
) {

    fun generateToken(id: UUID, userId: Int, username: String) =
        JWT.create()
            .withKeyId(id.toString())
            .withClaim(CLAIM_USER_ID, userId)
            .withClaim(CLAIM_USERNAME, username)
            .withExpiresAt(Instant.now().plusSeconds(tokenTtl))
            .sign(Algorithm.HMAC256(secret))

    fun parseTokenInfo(token: String): TokenInfoDto {
        val jwt = JWT.require(Algorithm.HMAC256(secret))
            .build()
            .verify(token)

        val userId = jwt.getClaim(CLAIM_USER_ID).asInt()
        val username = jwt.getClaim(CLAIM_USERNAME).asString()
        val uuid = jwt.keyId

        return TokenInfoDto(
            userId = userId,
            username = username,
            uuid = uuid
        )
    }

}