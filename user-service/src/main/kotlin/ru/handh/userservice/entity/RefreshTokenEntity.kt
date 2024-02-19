package ru.handh.userservice.entity

import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "refresh_token")
data class RefreshTokenEntity(
    @Id
    val id: UUID,

    @Column(name = "access_token_id", nullable = false)
    val accessTokenId: UUID,

    @Column(name = "expires_at", columnDefinition = "TIMESTAMP")
    val expiresAt: LocalDateTime
)
