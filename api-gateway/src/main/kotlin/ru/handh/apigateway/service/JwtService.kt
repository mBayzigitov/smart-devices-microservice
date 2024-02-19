package ru.handh.apigateway.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.handh.apigateway.dto.TokenInfoDto

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secret: String
) {

    companion object {
        const val CLAIM_USERNAME = "username"
        const val CLAIM_USER_ID = "userId"
    }

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