package ru.handh.userservice.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.handh.userservice.repository.RefreshTokenRepository
import javax.transaction.Transactional

@Component
class ScheduledTask(
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Scheduled(cron = "0 0 6 * * *") // every day at 6AM
    @Transactional
    fun scheduleExpiredTokensDeletion() =
        refreshTokenRepository.deleteByExpiresAtBefore()

}