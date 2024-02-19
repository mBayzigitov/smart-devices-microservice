package ru.handh.userservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import ru.handh.userservice.entity.RefreshTokenEntity
import java.time.LocalDateTime
import java.util.UUID

interface RefreshTokenRepository: JpaRepository<RefreshTokenEntity, UUID> {
    fun findByAccessTokenId(accessTokenId: UUID): RefreshTokenEntity?

    @Modifying
    fun deleteByExpiresAtBefore(expiresAt: LocalDateTime = LocalDateTime.now())
}