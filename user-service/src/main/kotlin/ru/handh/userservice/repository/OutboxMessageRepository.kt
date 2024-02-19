package ru.handh.userservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.handh.userservice.entity.OutboxMessageEntity

interface OutboxMessageRepository : JpaRepository<OutboxMessageEntity, Int> {
}