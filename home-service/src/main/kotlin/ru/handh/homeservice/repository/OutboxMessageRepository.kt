package ru.handh.homeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.handh.homeservice.entity.OutboxMessageEntity

interface OutboxMessageRepository : JpaRepository<OutboxMessageEntity, Int> {
}